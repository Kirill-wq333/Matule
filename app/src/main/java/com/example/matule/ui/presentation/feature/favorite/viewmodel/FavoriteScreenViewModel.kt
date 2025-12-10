package com.example.matule.ui.presentation.feature.favorite.viewmodel

import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.favorite.interactor.FavoriteInteractor
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.favorite.repository.FavoriteRepository
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
    private val favoriteRepository: FavoriteRepository,
    private val cartInteractor: CartInteractor
)  : BaseViewModel<FavoriteScreenContract.Event, FavoriteScreenContract.State, FavoriteScreenContract.Effect>(){

    override fun setInitialState(): FavoriteScreenContract.State = FavoriteScreenContract.State.Loading

    override fun handleEvent(event: FavoriteScreenContract.Event) = when(event) {
        is FavoriteScreenContract.Event.RefreshContent -> refreshFavorite()
        is FavoriteScreenContract.Event.LoadedContent -> loadFavorite()
        is FavoriteScreenContract.Event.ToggleProductFavorite -> toggleFavorite(event.productId)
        is FavoriteScreenContract.Event.AddToCart -> addToCart(event.productId,event.quantity)
    }

    init {
        handleEvent(FavoriteScreenContract.Event.LoadedContent)
    }

    private fun toggleFavorite(productId: Long) {
        viewModelScope.launch(dispatcher) {
            val currentState = currentState
            if (currentState is FavoriteScreenContract.State.Loaded) {
                val updatedFavorites = currentState.favorite.filter {
                    it.product.id != productId
                }

                if (updatedFavorites.isEmpty()) {
                    setState(FavoriteScreenContract.State.Empty)
                } else {
                    setState(
                        currentState.copy(favorite = updatedFavorites)
                    )
                }
            }

            val result = favoriteRepository.removeFromFavorites(productId)

            if (result.isSuccess) {
                when (val favoriteResult = result.getOrNull()) {
                    is FavoriteResult.Success -> {
                        if (!favoriteResult.isFavorite) {
                            setEffect {
                                FavoriteScreenContract.Effect.FavoriteStatusUpdated(
                                    result = favoriteResult.copy(
                                        productId = favoriteResult.productId,
                                        isFavorite = false
                                    ),
                                )
                            }
                        }
                    }
                    is FavoriteResult.Error -> {
                        setEffect {
                            FavoriteScreenContract.Effect.ShowError(favoriteResult.message)
                        }
                        loadFavorite()
                    }
                    else -> {}
                }
            } else {
                setEffect {
                    FavoriteScreenContract.Effect.ShowError(
                        result.exceptionOrNull()?.message ?: "Ошибка сети"
                    )
                }
                loadFavorite()
            }
        }
    }
    private fun loadFavorite(){
        viewModelScope.launch(dispatcher) {
            setState(FavoriteScreenContract.State.Loading)

            val result = favoriteInteractor.getFavorite()
            authInteractor.isUserLoggedIn()

            if (result.isSuccess){
                val favorite = result.getOrNull()!!

                val cartResult = cartInteractor.getCart()
                if (favorite.isEmpty()) {
                    setState(FavoriteScreenContract.State.Empty)
                } else {
                    cartResult.fold(
                        onSuccess = { cartState ->
                            val cartItemIds = cartState.items.map { it.productId }.toSet()
                            setState(FavoriteScreenContract.State.Loaded(
                                favorite = favorite,
                                cartItems = cartItemIds
                            ))
                        },
                        onFailure = {
                            val localCartItems = cartInteractor.getLocalCartItems().getOrNull() ?: emptySet()

                            setState(FavoriteScreenContract.State.Loaded(
                                favorite = favorite,
                                cartItems = localCartItems
                            ))
                        }
                    )
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



    private fun addToCart(productId: Long, quantity: Int = 1) {
        viewModelScope.launch(dispatcher) {

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