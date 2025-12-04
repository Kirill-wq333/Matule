package com.example.matule.ui.presentation.feature.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.main.interactor.MainInteractor
import com.example.domain.ui.presentation.feature.popular.interactor.PopularInteractor
import com.example.domain.ui.presentation.feature.popular.model.Product
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

        viewModelScope.launch {
            favoriteInteractor.favoriteUpdates.collect { (productId, isFavorite) ->
                updateProductFavoriteStatus(productId, isFavorite)
            }
        }

        checkAuthAndLoadContent()
    }

    private fun checkAuthAndLoadContent() {
        viewModelScope.launch {
            val isLoggedIn = authInteractor.isUserLoggedIn()

            if (isLoggedIn) {
                loadHomeContent()
            }
        }
    }


    private fun loadHomeContent() {
        viewModelScope.launch {
            setState ( MainScreenContract.State.Loading )

            val result = mainInteractor.loadHomeContent()
            if (result.isSuccess) {
                val content = result.getOrNull()!!

                val cart = cartInteractor.getLocalCartItems()

                setState (
                    MainScreenContract.State.Loaded(
                        categories = content.categories,
                        cartItems = cart,
                        popularProducts = content.popularProducts,
                        promotions = content.promotions
                    )
                )
            } else {
                val error = result.exceptionOrNull()!!
                setState ( MainScreenContract.State.Error(error.message ?: "Ошибка загрузки данных") )
                setEffect { MainScreenContract.Effect.ShowError("Не удалось загрузить данные") }
            }
        }
    }

    private fun toggleProductFavorite(productId: Long, currentlyFavorite: Boolean) {
        viewModelScope.launch {

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

    private fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            setState ( MainScreenContract.State.Loading )

            val result = popularInteractor.loadProductsByCategory(category)

            if (result.isSuccess) {
                val products = result.getOrNull()!!

                setEffect { MainScreenContract.Effect.CategoryProductsLoaded(category, products) }

                val currentState = currentState
                if (currentState is MainScreenContract.State.Loaded) {
                    setState (
                        currentState.copy(
                            popularProducts = products,
                            selectedCategoryId = currentState.categories.find { it.slug == category }?.id
                        )
                    )
                }
            } else {
                setEffect { MainScreenContract.Effect.ShowError("Не удалось загрузить товары категории") }
                setState ( MainScreenContract.State.Loaded() )
            }
        }
    }

    private fun selectCategory(categoryId: Long) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val category = currentState.categories.find { it.id == categoryId }
            category?.let {
                setState ( currentState.copy(selectedCategoryId = categoryId) )
                setEvent(MainScreenContract.Event.LoadProductsByCategory(it.slug))
            }
        }
    }

    private fun refreshContent() {
        setState(MainScreenContract.State.Loading)
    }

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
        viewModelScope.launch {

            val isLoggedIn = authInteractor.isUserLoggedIn()
            val token = authInteractor.getToken()


            if (!isLoggedIn || token == null) {
                return@launch
            }

            proceedWithAddToCart(productId, quantity)
        }
    }

    private suspend fun proceedWithAddToCart(productId: Long, quantity: Int = 1) {

        updateCartIconState(productId, true)
        incrementCartCountOptimistic(quantity)

        val result = cartInteractor.addToCartSimple(productId, quantity)

        if (result.isSuccess && result.getOrNull() == true) {
            setEffect { MainScreenContract.Effect.CartItemAdded(productId) }
        } else {
            updateCartIconState(productId, false)
            decrementCartCountOptimistic(quantity)

        }
    }

    private fun updateCartIconState(productId: Long, inCart: Boolean) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val updatedCartItems = if (inCart) {
                currentState.cartItems + productId
            } else {
                currentState.cartItems - productId
            }

            setState(
                currentState.copy(
                    cartItems = updatedCartItems
                )
            )
        }
    }

    private fun incrementCartCountOptimistic(quantity: Int) {
        val currentState = currentState
        if (currentState is MainScreenContract.State.Loaded) {
            val currentTotal = currentState.cartState.totalItems
            setState (
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
            setState (
                currentState.copy(
                    cartState = currentState.cartState.copy(
                        totalItems = max(0, currentTotal - quantity)
                    )
                )
            )
        }
    }

    fun getProductById(productId: Long): Product? {
        val currentState = currentState
        return if (currentState is MainScreenContract.State.Loaded) {
            currentState.popularProducts.find { it.id == productId }
        } else {
            null
        }
    }

}