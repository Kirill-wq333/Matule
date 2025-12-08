package com.example.matule.ui.presentation.feature.cart.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
    private val authInteractor: AuthInteractor
) : BaseViewModel<CartScreenContract.Event, CartScreenContract.State, CartScreenContract.Effect>() {

    override fun setInitialState(): CartScreenContract.State = CartScreenContract.State.Loading

    override fun handleEvent(event: CartScreenContract.Event) {
        when (event) {
            is CartScreenContract.Event.LoadCart -> loadCart()
            is CartScreenContract.Event.UpdateQuantity -> updateQuantity(event.cartItemId, event.newQuantity)
            is CartScreenContract.Event.RemoveItem -> removeItem(event.cartItemId)
        }
    }
    init {
        checkAuthAndLoadContent()
    }

    private fun checkAuthAndLoadContent() {
        viewModelScope.launch(dispatcher) {
            val isLoggedIn = authInteractor.isUserLoggedIn()

            if (isLoggedIn) {
                loadCart()
            }
        }
    }

    private fun loadCart() {
        viewModelScope.launch(dispatcher) {
            setState ( CartScreenContract.State.Loading )

            cartInteractor.getCart().fold(
                onSuccess = { cartState ->
                    if (cartState.items.isNotEmpty()) {
                        setState (
                            CartScreenContract.State.Loaded(
                                items = cartState.items,
                                totalPrice = cartState.totalPrice,
                                subtotal = cartState.subtotal,
                                delivery = cartState.delivery
                            )
                        )
                    } else {
                        setState ( CartScreenContract.State.Empty )
                    }
                },
                onFailure = { error ->
                    setEffect { CartScreenContract.Effect.ShowSnackbar("Ошибка загрузки: ${error.message}") }
                    setState ( CartScreenContract.State.Empty )
                }
            )
        }
    }

    private fun updateQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch(dispatcher) {
            cartInteractor.updateCartItem(cartItemId, newQuantity).fold(
                onSuccess = { cartResult ->
                    when (cartResult) {
                        is CartResult.Success -> {
                            loadCart()
                            setEffect {
                                CartScreenContract.Effect.ShowToast("Количество обновлено")
                            }
                        }
                        is CartResult.Error -> {
                            setEffect {
                                CartScreenContract.Effect.ShowSnackbar(cartResult.message)
                            }
                        }
                    }
                },
                onFailure = { error ->
                    setEffect {
                        CartScreenContract.Effect.ShowSnackbar("Ошибка: ${error.message}")
                    }
                }
            )
        }
    }

    private fun removeItem(cartItemId: Long) {
        viewModelScope.launch(dispatcher) {
            cartInteractor.removeFromCart(cartItemId).fold(
                onSuccess = { cartResult ->
                    when (cartResult) {
                        is CartResult.Success -> {
                            loadCart()
                            setEffect {
                                CartScreenContract.Effect.ShowToast("Товар удален")
                            }
                        }
                        is CartResult.Error -> {
                            setEffect {
                                CartScreenContract.Effect.ShowSnackbar(cartResult.message)
                            }
                        }
                    }
                },
                onFailure = { error ->
                    setEffect {
                        CartScreenContract.Effect.ShowSnackbar("Ошибка удаления: ${error.message}")
                    }
                }
            )
        }
    }

}