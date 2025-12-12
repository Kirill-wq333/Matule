package com.example.matule.ui.presentation.feature.orders.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.orders.interactor.OrdersInteractor
import com.example.domain.ui.presentation.feature.orders.model.CreateOrderRequest
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersScreenViewModel @Inject constructor(
    private val ordersInteractor: OrdersInteractor,
    private val authInteractor: AuthInteractor
) : BaseViewModel<OrdersScreenContract.Event, OrdersScreenContract.State, OrdersScreenContract.Effect>() {

    override fun setInitialState(): OrdersScreenContract.State = OrdersScreenContract.State.Loading

    override fun handleEvent(event: OrdersScreenContract.Event) = when (event) {
        is OrdersScreenContract.Event.LoadOrders -> loadOrders()
        is OrdersScreenContract.Event.LoadOrderDetails -> loadOrderDetails(event.orderId)
        is OrdersScreenContract.Event.UpdateOrderStatus -> updateOrderStatus(
            event.orderId,
            event.status
        )

        is OrdersScreenContract.Event.DeleteOrder -> deleteOrder(event.orderId)
        is OrdersScreenContract.Event.Refresh -> refresh()
    }
    init {
        checkAuthAndLoadContent()
    }

    private fun checkAuthAndLoadContent() {
        viewModelScope.launch(dispatcher) {
            val isLoggedIn = authInteractor.isUserLoggedIn()

            if (isLoggedIn) {
                loadOrders()
            }
        }
    }

    private fun loadOrders() {
        viewModelScope.launch(dispatcher) {
            setState(OrdersScreenContract.State.Loading)

            ordersInteractor.getOrders().fold(
                onSuccess = { orders ->
                    setState(
                        OrdersScreenContract.State.OrdersLoaded(
                            orders = orders,
                            isLoading = false,
                            error = null
                        )
                    )
                },
                onFailure = { error ->
                    setState(
                        OrdersScreenContract.State.Error(
                            errorMessage = error.message ?: "Не удалось загрузить заказы",
                            isLoading = false
                        )
                    )
                }
            )
        }
    }

    private fun loadOrderDetails(orderId: Long) {
        viewModelScope.launch(dispatcher) {
            setState(OrdersScreenContract.State.Loading)

            authInteractor.isUserLoggedIn()
            ordersInteractor.getOrder(orderId).fold(
                onSuccess = { order ->
                    setState(
                        OrdersScreenContract.State.OrderDetailsLoaded(
                            order = order,
                            isLoading = false,
                            error = null
                        )
                    )
                },
                onFailure = { error ->
                    setState(
                        OrdersScreenContract.State.Error(
                            errorMessage = error.message ?: "Не удалось загрузить детали заказа",
                            isLoading = false
                        )
                    )
                }
            )
        }
    }

    private fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        viewModelScope.launch(dispatcher) {
            ordersInteractor.updateOrdersStatus(orderId, status).fold(
                onSuccess = {
                    setEffect {
                        OrdersScreenContract.Effect.OrderStatusUpdated(
                            orderId = orderId,
                            newStatus = status,
                            message = "Статус заказа обновлен"
                        )
                    }
                    loadOrderDetails(orderId)
                },
                onFailure = { error ->
                    setEffect {
                        OrdersScreenContract.Effect.ShowError(
                            message = error.message ?: "Не удалось обновить статус заказа"
                        )
                    }
                }
            )
        }
    }

    private fun deleteOrder(orderId: Long) {
        viewModelScope.launch(dispatcher) {
            setState(OrdersScreenContract.State.Loading)

            ordersInteractor.deleteOrder(orderId).fold(
                onSuccess = {
                    setEffect {
                        OrdersScreenContract.Effect.OrderDeleted(
                            orderId = orderId,
                            message = "Заказ успешно удален"
                        )
                    }
                    loadOrders()
                },
                onFailure = { error ->
                    setState(
                        OrdersScreenContract.State.Error(
                            errorMessage = error.message ?: "Не удалось удалить заказ",
                            isLoading = false
                        )
                    )
                }
            )
        }
    }

    private fun refresh() {
        loadOrders()
    }
}