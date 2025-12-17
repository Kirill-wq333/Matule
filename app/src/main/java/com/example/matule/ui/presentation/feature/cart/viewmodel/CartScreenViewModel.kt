package com.example.matule.ui.presentation.feature.cart.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.domain.ui.presentation.feature.cart.model.CartResult
import com.example.domain.ui.presentation.feature.orders.interactor.OrdersInteractor
import com.example.domain.ui.presentation.feature.orders.model.CreateOrderRequest
import com.example.domain.ui.presentation.feature.profile.interactor.ProfileInteractor
import com.example.domain.ui.presentation.feature.profile.model.ProfileResult
import com.example.domain.ui.presentation.feature.profile.model.UserProfile
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val cartInteractor: CartInteractor,
    private val authInteractor: AuthInteractor,
    private val ordersInteractor: OrdersInteractor
) : BaseViewModel<CartScreenContract.Event, CartScreenContract.State, CartScreenContract.Effect>() {

    private val _profile: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile())
    val profile = _profile.asStateFlow()

    override fun setInitialState(): CartScreenContract.State = CartScreenContract.State.Loading

    override fun handleEvent(event: CartScreenContract.Event) {
        when (event) {
            is CartScreenContract.Event.LoadCart -> loadCart()
            is CartScreenContract.Event.UpdateQuantity -> updateQuantity(event.cartItemId, event.newQuantity)
            is CartScreenContract.Event.RemoveItem -> removeItem(event.cartItemId)
            is CartScreenContract.Event.LoadCartProfile -> loadProfile()
            is CartScreenContract.Event.CreateOrder -> createOrder(event.request)
            is CartScreenContract.Event.Refresh -> refresh()
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
                loadProfile()
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch(dispatcher) {
            setState (CartScreenContract.State.Loading )

            val result = profileInteractor.getProfile()
            if (result.isSuccess) {
                when (val profileResult = result.getOrNull()!!) {
                    is ProfileResult.Success -> {
                        _profile.emit(profileResult.profile)
                        setState (CartScreenContract.State.Loaded())
                    }
                    is ProfileResult.Error -> {
                        setEffect { CartScreenContract.Effect.ShowSnackbar(profileResult.message) }
                    }
                }
            } else {
                setEffect { CartScreenContract.Effect.ShowSnackbar("Ошибка загрузки профиля") }
            }
        }
    }

    private fun refresh() = setState(CartScreenContract.State.Loading)

    private fun loadCart() {
        viewModelScope.launch(dispatcher) {
            setState(CartScreenContract.State.Loading)

            cartInteractor.getCart().fold(
                onSuccess = { cartState ->
                        setState(
                            CartScreenContract.State.Loaded(
                                items = cartState.items,
                                totalPrice = cartState.totalPrice,
                                subtotal = cartState.subtotal,
                                delivery = cartState.delivery
                            )
                        )

                },
                onFailure = { error ->
                    setEffect { CartScreenContract.Effect.ShowSnackbar("Ошибка загрузки: ${error.message}") }
                    setState(CartScreenContract.State.Empty)
                }
            )
        }
    }

    private fun createOrder(request: CreateOrderRequest) {
        viewModelScope.launch(dispatcher) {

            ordersInteractor.createdOrder(request).fold(
                onSuccess = { order ->
                    if (!authInteractor.isUserLoggedIn()) {
                        setEffect {
                            CartScreenContract.Effect.OrderCreated(
                                order = order,
                                message = "Заказ успешно создан"
                            )
                        }
                        return@launch
                    }
                },
                onFailure = { error ->
                    setEffect {
                        CartScreenContract.Effect.ShowSnackbar(
                            message = error.message ?: "Ошибка при создании заказа"
                        )
                    }
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