package com.example.matule.ui.presentation.feature.favorite.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.matule.ui.core.viewmodel.BaseViewModel
import com.example.matule.ui.presentation.feature.notification.viewmodel.NotificationScreenContract
import com.example.matule.ui.presentation.feature.popular.viewmodel.PopularScreenContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val favoriteInteractor: FavoriteInteractor,
    private val authInteractor: AuthInteractor,
    private val cartInteractor: CartInteractor
)  : BaseViewModel<FavoriteScreenContract.Event, FavoriteScreenContract.State, FavoriteScreenContract.Effect>(){

    override fun setInitialState(): FavoriteScreenContract.State = FavoriteScreenContract.State.Loading

    override fun handleEvent(event: FavoriteScreenContract.Event) = when(event) {
        is FavoriteScreenContract.Event.RefreshContent -> refreshFavorite()
        is FavoriteScreenContract.Event.LoadedContent -> loadFavorite()
        is FavoriteScreenContract.Event.ToggleProductFavorite -> toggleProductFavorite(event.productId,event.currentlyFavorite)
        is FavoriteScreenContract.Event.AddToCart -> addToCart(event.productId,event.quantity)
    }

    init {
        handleEvent(FavoriteScreenContract.Event.LoadedContent)
    }

    private fun loadFavorite(){
        viewModelScope.launch {
            setState(FavoriteScreenContract.State.Loading)

            val result = favoriteInteractor.getFavorite()
            authInteractor.isUserLoggedIn()

            if (result.isSuccess){
                val favorite = result.getOrNull()!!

                val cartItems = cartInteractor.getLocalCartItems()
                if (favorite.isEmpty()) {
                    setState(FavoriteScreenContract.State.Empty)
                } else {
                    setState(FavoriteScreenContract.State.Loaded(
                        favorite = favorite,
                        cartItems = cartItems
                    ))
                }
            } else {
                val error = result.exceptionOrNull()!!
                val errorMessage = error.message ?: "Ошибка загрузки"
                setState(FavoriteScreenContract.State.Error(errorMessage))
                setEffect {
                    FavoriteScreenContract.Effect.ShowError("Не удалось загрузить уведомления: $errorMessage")
                }
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
                        setEffect { FavoriteScreenContract.Effect.FavoriteStatusUpdated(favoriteResult) }
                        updateProductFavoriteStatus(favoriteResult.productId, favoriteResult.isFavorite)
                    }
                    is FavoriteResult.Error -> {
                        updateProductFavoriteStatus(productId, currentlyFavorite)
                        setEffect { FavoriteScreenContract.Effect.ShowError(favoriteResult.message) }
                    }
                }
            } else {
                updateProductFavoriteStatus(productId, currentlyFavorite)
                setEffect { FavoriteScreenContract.Effect.ShowError("Ошибка обновления избранного") }
            }
        }
    }

    private fun updateProductFavoriteStatusOptimistic(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is FavoriteScreenContract.State.Loaded) {
            setState(
                currentState.copy(
                    favorite = currentState.favorite.map { favoriteItem ->
                        if (favoriteItem.product.id == productId) {
                            favoriteItem.copy(product = favoriteItem.product.copy(isFavorite = isFavorite))
                        } else {
                            favoriteItem
                        }
                    }
                )
            )
        }
    }
    private fun updateProductFavoriteStatus(productId: Long, isFavorite: Boolean) {
        val currentState = currentState
        if (currentState is FavoriteScreenContract.State.Loaded) {
            setState(
                currentState.copy(
                    favorite = currentState.favorite.map { favoriteItem ->
                        if (favoriteItem.product.id == productId) {
                            favoriteItem.copy(product = favoriteItem.product.copy(isFavorite = isFavorite))
                        } else {
                            favoriteItem
                        }
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

        val userId = authInteractor.getCurrentUserId()

        if (userId == null) {
            updateCartIconState(productId, false)
            decrementCartCountOptimistic(quantity)
            return
        }
        val result = cartInteractor.addToCartSimple(productId, quantity)

        if (result.isSuccess && result.getOrNull() == true) {
            setEffect { FavoriteScreenContract.Effect.CartItemAdded(productId) }
        } else {
            updateCartIconState(productId, false)
            decrementCartCountOptimistic(quantity)

        }
    }

    private fun updateCartIconState(productId: Long, inCart: Boolean) {
        val currentState = currentState
        if (currentState is FavoriteScreenContract.State.Loaded) {
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
        if (currentState is FavoriteScreenContract.State.Loaded) {
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
        if (currentState is FavoriteScreenContract.State.Loaded) {
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

    private fun refreshFavorite() {
        loadFavorite()
    }
}