package com.example.domain.ui.presentation.feature.orders.repository

import com.example.domain.ui.presentation.feature.orders.model.CreateOrderRequest
import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.domain.ui.presentation.feature.orders.model.OrdersSummary

interface OrdersRepository {
    suspend fun getOrders(): Result<List<Order>>
    suspend fun getOrder(orderId: Long): Result<Order>
    suspend fun createOrder(request: CreateOrderRequest): Result<Order>
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus): Result<Unit>
    suspend fun deleteOrder(orderId: Long): Result<Unit>
    suspend fun getOrdersSummary(): Result<OrdersSummary>
}