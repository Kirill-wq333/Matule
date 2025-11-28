package com.example.matule.ui.presentation.feature.popular.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.popular.interactor.PopularInteractor
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.ui.core.viewmodel.BaseViewModel
import com.example.matule.ui.presentation.feature.main.viewmodel.MainScreenContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class PopularScreenViewModel @Inject constructor(
    private val popularInteractor: PopularInteractor,
    private val cartInteractor: CartInteractor,
    private val authInteractor: AuthInteractor,
    private val favoriteInteractor: FavoriteInteractor
): BaseViewModel<PopularScreenContract.Event, PopularScreenContract.State, PopularScreenContract.Effect>() {

    override fun setInitialState(): PopularScreenContract.State = PopularScreenContract.State.Loading

    override fun handleEvent(event: PopularScreenContract.Event) = when (event) {
        is PopularScreenContract.Event.LoadedContent -> loadPopular()
        is PopularScreenContract.Event.RefreshContent -> refreshContent()
        is PopularScreenContract.Event.AddToCart -> addToCart(event.productId,event.quantity)
        is PopularScreenContract.Event.ToggleProductFavorite -> toggleProductFavorite(event.productId,event.currentlyFavorite)
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
                cartInteractor.getLocalCartItems()
                loadPopular()
            }
        }
    }

    private fun refreshContent() {
        loadPopular()
    }

    private fun loadPopular(){
        viewModelScope.launch {
            setState(PopularScreenContract.State.Loading)

            val result = popularInteractor.loadAllProducts()

            if (result.isSuccess) {
                val content = result.getOrNull()!!

                val cart = cartInteractor.getLocalCartItems()
                setState(
                    PopularScreenContract.State.Loaded(
                        popularProducts = content,
                        cartItems = cart
                    )
                )
            } else {
                val error = result.exceptionOrNull()!!
                setState(
                    PopularScreenContract.State.Error(
                        error.message ?: "Ошибка загрузки данных"
                    )
                )
                setEffect { PopularScreenContract.Effect.ShowError("Не удалось загрузить данные") }
            }

        }
    }

    private fun toggleProductFavorite(productId: Long, currentlyFavorite: Boolean) {
        viewModelScope.launch {

            updateProductFavoriteStatusOptimistic(productId, !currentlyFavorite)

            val result = favoriteInteractor.toggleFavorite(productId, currentlyFavorite)

            if (result.isSuccess) {
                val favoriteResult = result.getOrNull()!!
                when (favoriteResult) {
                    is FavoriteResult.Success -> {
                        setEffect { PopularScreenContract.Effect.FavoriteStatusUpdated(favoriteResult) }
                        updateProductFavoriteStatus(favoriteResult.productId, favoriteResult.isFavorite)
                    }
                    is FavoriteResult.Error -> {
                        updateProductFavoriteStatus(productId, currentlyFavorite)
                        setEffect { PopularScreenContract.Effect.ShowError(favoriteResult.message) }
                    }
                }
            } else {
                updateProductFavoriteStatus(productId, currentlyFavorite)
                setEffect { PopularScreenContract.Effect.ShowError("Ошибка обновления избранного") }
            }
        }
    }

    private fun updateProductFavoriteStatusOptimistic(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is PopularScreenContract.State.Loaded) {
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
        if (currentState is PopularScreenContract.State.Loaded) {
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
            setEffect { PopularScreenContract.Effect.CartItemAdded(productId) }
        } else {
            updateCartIconState(productId, false)
            decrementCartCountOptimistic(quantity)

        }
    }

    private fun updateCartIconState(productId: Long, inCart: Boolean) {
        val currentState = currentState
        if (currentState is PopularScreenContract.State.Loaded) {
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
        if (currentState is PopularScreenContract.State.Loaded) {
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
        if (currentState is PopularScreenContract.State.Loaded) {
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

}