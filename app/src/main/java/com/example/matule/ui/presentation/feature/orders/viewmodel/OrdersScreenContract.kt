package com.example.matule.ui.presentation.feature.orders.viewmodel

import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object OrdersScreenContract {

    sealed interface Event : ViewEvent {
        data object LoadOrders : Event
        data object Refresh : Event
        data class LoadOrderDetails(val orderId: Long) : Event
        data class UpdateOrderStatus(val orderId: Long, val status: OrderStatus) : Event
        data class DeleteOrder(val orderId: Long) : Event
    }

    sealed interface State : ViewState {
        data object Loading : State
        data object Empty : State

        data class OrdersLoaded(
            val orders: List<Order>,
            val isLoading: Boolean = false,
            val error: String? = null
        ) : State

        data class OrderDetailsLoaded(
            val order: Order,
            val isLoading: Boolean = false,
            val error: String? = null
        ) : State

        data class Error(
            val errorMessage: String,
            val isLoading: Boolean = false
        ) : State
    }

    sealed interface Effect : ViewEffect {

        data class OrderDeleted(
            val orderId: Long,
            val message: String
        ) : Effect

        data class OrderStatusUpdated(
            val orderId: Long,
            val newStatus: OrderStatus,
            val message: String
        ) : Effect

        data class ShowError(
            val message: String
        ) : Effect

    }
}