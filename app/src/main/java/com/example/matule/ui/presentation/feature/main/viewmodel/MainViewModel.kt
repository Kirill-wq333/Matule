package com.example.matule.ui.presentation.feature.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.main.interactor.MainInteractor
import com.example.domain.ui.presentation.feature.popular.interactor.PopularInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor,
    private val cartInteractor: CartInteractor,
    private val authInteractor: AuthInteractor,
    private val popularInteractor: PopularInteractor,
    private val favoriteInteractor: FavoriteInteractor,
) : BaseViewModel<MainScreenContract.Event, MainScreenContract.State, MainScreenContract.Effect>() {

    override fun setInitialState(): MainScreenContract.State = MainScreenContract.State.Loading

    override fun handleEvent(event: MainScreenContract.Event) = when (event) {
        is MainScreenContract.Event.LoadContent -> loadHomeContent()
        is MainScreenContract.Event.ToggleProductFavorite -> toggleProductFavorite(event.productId, event.currentlyFavorite)
        is MainScreenContract.Event.SelectCategory -> selectCategory(event.categoryId)
        is MainScreenContract.Event.LoadProductsByCategory -> loadProductsByCategory(event.category)
        is MainScreenContract.Event.RefreshContent -> refreshContent()
        is MainScreenContract.Event.AddToCart -> addToCart(event.productId,event.quantity)
    }

    init {

        viewModelScope.launch(dispatcher) {
            favoriteInteractor.favoriteUpdates.collect { (productId, isFavorite) ->
                updateProductFavoriteStatus(productId, isFavorite)
            }
        }

        checkAuthAndLoadContent()
    }

    private fun checkAuthAndLoadContent() {
        viewModelScope.launch(dispatcher) {
            val isLoggedIn = authInteractor.isUserLoggedIn()

            if (isLoggedIn) {
                loadHomeContent()
            }
        }
    }


    private fun loadHomeContent() {
        viewModelScope.launch(dispatcher) {
            setState(MainScreenContract.State.Loading)

            val result = mainInteractor.loadHomeContent()
            if (result.isSuccess) {
                val content = result.getOrNull()!!

                val cartResult = cartInteractor.getCart()

                cartResult.fold(
                    onSuccess = { cartState ->
                        val cartItemIds = cartState.items.map { it.productId }.toSet()

                        setState(
                            MainScreenContract.State.Loaded(
                                categories = content.categories,
                                isEnableDot = cartItemIds,
                                selectedCategoryId = null,
                                popularProducts = content.popularProducts,
                                promotions = content.promotions,
                                cartState = cartState
                            )
                        )
                    },
                    onFailure = {
                        val localCartItems = cartInteractor.getLocalCartItems().getOrNull() ?: emptySet()

                        setState(
                            MainScreenContract.State.Loaded(
                                categories = content.categories,
                                isEnableDot = localCartItems,
                                selectedCategoryId = null,
                                popularProducts = content.popularProducts,
                                promotions = content.promotions,
                            )
                        )
                    }
                )
            } else {
                val error = result.exceptionOrNull()!!
                setState(MainScreenContract.State.Error(error.message ?: "Ошибка загрузки данных"))
                setEffect { MainScreenContract.Effect.ShowError("Не удалось загрузить данные") }
            }
        }
    }

    private fun toggleProductFavorite(productId: Long, currentlyFavorite: Boolean) {
        viewModelScope.launch(dispatcher) {

            updateProductFavoriteStatusOptimistic(productId, !currentlyFavorite)

            val result = favoriteInteractor.toggleFavorite(productId, currentlyFavorite)

            if (result.isSuccess) {
                when (val favoriteResult = result.getOrNull()!!) {
                    is FavoriteResult.Success -> {
                        setEffect { MainScreenContract.Effect.FavoriteStatusUpdated(favoriteResult) }
                        updateProductFavoriteStatus(favoriteResult.productId, favoriteResult.isFavorite)
                    }
                    is FavoriteResult.Error -> {
                        updateProductFavoriteStatus(productId, currentlyFavorite)
                        setEffect { MainScreenContract.Effect.ShowError(favoriteResult.message) }
                    }
                }
            } else {
                updateProductFavoriteStatus(productId, currentlyFavorite)
                setEffect { MainScreenContract.Effect.ShowError("Ошибка обновления избранного") }
            }
        }
    }

    private fun selectCategory(categoryId: Long) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val category = currentState.categories.find { it.id == categoryId }
            category?.let {
                setState(currentState.copy(selectedCategoryId = categoryId))

                viewModelScope.launch(dispatcher) {
                    loadProductsByCategory(it.slug)
                }
            }
        }
    }

    private fun loadProductsByCategory(category: String) {
        viewModelScope.launch(dispatcher) {
            setState(MainScreenContract.State.Loading)
            authInteractor.isUserLoggedIn()
            val result = popularInteractor.loadProductsByCategory(category)

            if (result.isSuccess) {
                val products = result.getOrNull()!!

                setEffect { MainScreenContract.Effect.CategoryProductsLoaded(category, products) }

                val currentState = currentState
                if (currentState is MainScreenContract.State.Loaded) {
                    setState(
                        currentState.copy(
                            popularProducts = products,
                            selectedCategoryId = currentState.categories.find { it.slug == category }?.id
                        )
                    )
                }
            } else {
                setEffect { MainScreenContract.Effect.ShowError("Не удалось загрузить товары категории") }
                val currentState = currentState
                if (currentState is MainScreenContract.State.Loaded) {
                    setState(currentState.copy(selectedCategoryId = null))
                }
            }
        }
    }

    private fun refreshContent() = setState(MainScreenContract.State.Loading)

    private fun updateProductFavoriteStatusOptimistic(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            setState(
                currentState.copy(
                    popularProducts = currentState.popularProducts.map { product ->
                        if (product.id == productId) product.copy(isFavorite = isFavorite) else product
                    }
                )
            )
        }
    }

    private fun updateProductFavoriteStatus(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            setState(
                currentState.copy(
                    popularProducts = currentState.popularProducts.map { product ->
                        if (product.id == productId) product.copy(isFavorite = isFavorite) else product
                    }
                )
            )
        }
    }

    private fun addToCart(productId: Long, quantity: Int = 1) {
        viewModelScope.launch(dispatcher) {
            val isLoggedIn = authInteractor.isUserLoggedIn()
            if (!isLoggedIn) {
                return@launch
            }

            proceedWithAddToCart(productId, quantity)
        }
    }

    private suspend fun proceedWithAddToCart(productId: Long, quantity: Int = 1) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val isAlreadyInCart = currentState.isEnableDot.contains(productId)

            if (isAlreadyInCart) {
                removeFromCart(productId, quantity)
            } else {
                addNewToCart(productId, quantity)
            }
        }
    }

    private suspend fun addNewToCart(productId: Long, quantity: Int = 1) {
        updateCartIconState(productId, true)
        incrementCartCountOptimistic(quantity)

        val result = cartInteractor.addToCartSimple(productId, quantity)

        result.fold(
            onSuccess = { success ->
                if (success) {
                    refreshCartState()
                    setEffect { MainScreenContract.Effect.CartItemAdded(productId) }
                } else {
                    updateCartIconState(productId, false)
                    decrementCartCountOptimistic(quantity)
                    setEffect { MainScreenContract.Effect.ShowError("Не удалось добавить товар в корзину") }
                }
            },
            onFailure = { error ->
                updateCartIconState(productId, false)
                decrementCartCountOptimistic(quantity)
                setEffect { MainScreenContract.Effect.ShowError("Ошибка: ${error.message}") }
            }
        )
    }

    private suspend fun removeFromCart(productId: Long, quantity: Int = 1) {
        updateCartIconState(productId, false)
        decrementCartCountOptimistic(quantity)

        val cartItemId = getCartItemIdForProduct(productId)

        if (cartItemId != null) {
            val result = cartInteractor.removeFromCart(cartItemId)

            result.fold(
                onSuccess = { cartResult ->
                    when (cartResult) {
                        is CartResult.Success -> {
                            refreshCartState()
                            setEffect { MainScreenContract.Effect.CartItemRemoved(productId) }
                        }
                        is CartResult.Error -> {
                            updateCartIconState(productId, true)
                            incrementCartCountOptimistic(quantity)
                            setEffect { MainScreenContract.Effect.ShowError(cartResult.message) }
                        }
                    }
                },
                onFailure = { error ->
                    updateCartIconState(productId, true)
                    incrementCartCountOptimistic(quantity)
                    setEffect { MainScreenContract.Effect.ShowError("Ошибка удаления: ${error.message}") }
                }
            )
        } else {
            refreshCartState()
            setEffect { MainScreenContract.Effect.ShowError("Товар не найден в корзине") }
        }
    }

    private fun getCartItemIdForProduct(productId: Long): Long? {
        val currentState = currentState
        return if (currentState is MainScreenContract.State.Loaded) {
            currentState.cartState.items
                .firstOrNull { it.productId == productId }
                ?.id
        } else {
            null
        }
    }

    private suspend fun refreshCartState() {
        cartInteractor.getCart().fold(
            onSuccess = { cartState ->
                val currentState = currentState
                if (currentState is MainScreenContract.State.Loaded) {
                    val updatedCartItems = cartState.items.map { it.productId }.toSet()

                    setState(
                        currentState.copy(
                            isEnableDot = updatedCartItems,
                            cartState = cartState
                        )
                    )
                }
            },
            onFailure = { error ->
                println("Не удалось обновить состояние корзины: ${error.message}")
            }
        )
    }

    private fun updateCartIconState(productId: Long, inCart: Boolean) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val updatedCartItems = if (inCart) {
                currentState.isEnableDot + productId
            } else {
                currentState.isEnableDot - productId
            }

            setState(
                currentState.copy(
                    isEnableDot = updatedCartItems
                )
            )
        }
    }

    private fun incrementCartCountOptimistic(quantity: Int) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val currentTotal = currentState.cartState.totalItems
            setState(
                currentState.copy(
                    cartState = currentState.cartState.copy(
                        totalItems = currentTotal + quantity
                    )
                )
            )
        }
    }

    private fun decrementCartCountOptimistic(quantity: Int) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val currentTotal = currentState.cartState.totalItems
            setState(
                currentState.copy(
                    cartState = currentState.cartState.copy(
                        totalItems = max(0, currentTotal - quantity)
                    )
                )
            )
        }
    }

}