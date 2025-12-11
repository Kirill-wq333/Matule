package com.example.data.ui.presentation.feature.orders.dto.response

import com.example.data.ui.presentation.feature.orders.dto.OrderDto
import com.example.data.ui.presentation.feature.orders.dto.OrdersSummaryDto

data class OrdersResponseDto(
    val success: Boolean,
    val orders: List<OrderDto>?,
    val summary: OrdersSummaryDto?
)

data class OrderResponseDto(
    val success: Boolean,
    val order: OrderDto?
)

data class CreateOrderResponseDto(
    val success: Boolean,
    val order: OrderDto?,
    val message: String?
)

data class UpdateStatusResponseDto(
    val success: Boolean,
    val message: String?
)

data class DeleteOrderResponseDto(
    val success: Boolean,
    val message: String?
)