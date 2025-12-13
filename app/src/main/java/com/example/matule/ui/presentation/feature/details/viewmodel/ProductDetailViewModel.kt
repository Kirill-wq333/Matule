package com.example.matule.ui.presentation.feature.details.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.popular.interactor.PopularInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val popularInteractor: PopularInteractor,
    private val authInteractor: AuthInteractor,
    private val cartInteractor: CartInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : BaseViewModel<ProductDetailContract.Event, ProductDetailContract.State, ProductDetailContract.Effect>() {

    override fun setInitialState(): ProductDetailContract.State =
        ProductDetailContract.State.Loading

    override fun handleEvent(event: ProductDetailContract.Event) = when (event) {
        is ProductDetailContract.Event.LoadContent -> loadProductId(event.productId)
        is ProductDetailContract.Event.Refresh -> refresh()
        is ProductDetailContract.Event.ToggleProductFavorite -> toggleProductFavorite(event.productId,event.currentlyFavorite)
        is ProductDetailContract.Event.AddToCart -> addToCart(event.productId)
    }

    private fun loadProductId(productId: Long) {
        viewModelScope.launch(dispatcher) {
            setState(ProductDetailContract.State.Loading)

            val result = popularInteractor.loadProductById(productId)
            authInteractor.isUserLoggedIn()

            if (result.isSuccess) {

                val product = result.getOrNull()!!
                val cartResult = cartInteractor.getCart()

                cartResult.fold(
                    onSuccess = {cartState ->
                        val cartItemIds = cartState.items.map { it.productId }.toSet()

                        setState(
                            ProductDetailContract.State.LoadProduct(
                                product = product,
                                isEnableDot = cartItemIds,
                                cartState = cartState
                            )
                        )
                    },
                    onFailure = {}
                )
            } else {
                setEffect { ProductDetailContract.Effect.ShowError("Не удалось загрузить данные") }
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
                        setEffect { ProductDetailContract.Effect.FavoriteStatusUpdated(favoriteResult) }
                        updateProductFavoriteStatus(favoriteResult.productId, favoriteResult.isFavorite)
                    }
                    is FavoriteResult.Error -> {
                        updateProductFavoriteStatus(productId, currentlyFavorite)
                        setEffect { ProductDetailContract.Effect.ShowError(favoriteResult.message) }
                    }
                }
            } else {
                updateProductFavoriteStatus(productId, currentlyFavorite)
                setEffect { ProductDetailContract.Effect.ShowError("Ошибка обновления избранного") }
            }
        }
    }

    private fun updateProductFavoriteStatusOptimistic(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is ProductDetailContract.State.LoadProduct) {
            setState(
                currentState.copy(
                    product = currentState.product.copy(
                        id = productId,
                        isFavorite = isFavorite
                    )
                )
            )
        }
    }

    private fun updateProductFavoriteStatus(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is ProductDetailContract.State.LoadProduct) {
            setState(
                currentState.copy(
                    product = currentState.product.copy(
                        id = productId,
                        isFavorite = isFavorite
                    )
                )
            )
        }
    }

    private fun refresh() = setState(ProductDetailContract.State.Loading)


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
        if (currentState is ProductDetailContract.State.LoadProduct) {
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
                    setEffect { ProductDetailContract.Effect.CartItemAdded(productId) }
                } else {
                    updateCartIconState(productId, false)
                    decrementCartCountOptimistic(quantity)
                    setEffect { ProductDetailContract.Effect.ShowError("Не удалось добавить товар в корзину") }
                }
            },
            onFailure = { error ->
                updateCartIconState(productId, false)
                decrementCartCountOptimistic(quantity)
                setEffect { ProductDetailContract.Effect.ShowError("Ошибка: ${error.message}") }
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
                            setEffect { ProductDetailContract.Effect.CartItemRemoved(productId) }
                        }
                        is CartResult.Error -> {
                            updateCartIconState(productId, true)
                            incrementCartCountOptimistic(quantity)
                            setEffect { ProductDetailContract.Effect.ShowError(cartResult.message) }
                        }
                    }
                },
                onFailure = { error ->
                    updateCartIconState(productId, true)
                    incrementCartCountOptimistic(quantity)
                    setEffect { ProductDetailContract.Effect.ShowError("Ошибка удаления: ${error.message}") }
                }
            )
        } else {
            refreshCartState()
            setEffect { ProductDetailContract.Effect.ShowError("Товар не найден в корзине") }
        }
    }

    private fun getCartItemIdForProduct(productId: Long): Long? {
        val currentState = currentState
        return if (currentState is ProductDetailContract.State.LoadProduct) {
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
                if (currentState is ProductDetailContract.State.LoadProduct) {
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
        if (currentState is ProductDetailContract.State.LoadProduct) {
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
        if (currentState is ProductDetailContract.State.LoadProduct) {
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
        if (currentState is ProductDetailContract.State.LoadProduct) {
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