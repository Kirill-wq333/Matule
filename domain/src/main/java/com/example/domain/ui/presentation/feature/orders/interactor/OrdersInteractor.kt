package com.example.domain.ui.presentation.feature.orders.interactor

import com.example.domain.ui.presentation.feature.orders.model.CreateOrderRequest
import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.domain.ui.presentation.feature.orders.model.OrdersSummary
import com.example.domain.ui.presentation.feature.orders.repository.OrdersRepository

class OrdersInteractor(
    private val ordersRepository: OrdersRepository
) {
    suspend fun getOrders(): Result<List<Order>> {
        return ordersRepository.getOrders()
    }

    suspend fun getOrder(orderId: Long): Result<Order>{
        return ordersRepository.getOrder(orderId)
    }

    suspend fun createdOrder(request: CreateOrderRequest): Result<Order>{
        return ordersRepository.createOrder(request)
    }

    suspend fun updateOrdersStatus(orderId: Long, status: OrderStatus): Result<Unit>{
        return ordersRepository.updateOrderStatus(orderId,status)
    }

    suspend fun deleteOrdersSummary(): Result<OrdersSummary> {
        return ordersRepository.getOrdersSummary()
    }

    suspend fun deleteOrder(orderId: Long): Result<Unit>{
        return ordersRepository.deleteOrder(orderId)
    }
}