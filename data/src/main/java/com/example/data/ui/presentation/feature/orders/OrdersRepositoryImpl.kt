package com.example.data.ui.presentation.feature.orders

import com.example.data.ui.presentation.feature.orders.datasource.OrdersApiService
import com.example.data.ui.presentation.feature.orders.dto.AddressDto
import com.example.data.ui.presentation.feature.orders.dto.ContactInfoDto
import com.example.data.ui.presentation.feature.orders.dto.OrderDto.Companion.toOrder
import com.example.data.ui.presentation.feature.orders.dto.OrdersSummaryDto.Companion.toOrdersSummary
import com.example.data.ui.presentation.feature.orders.dto.request.CreateOrderRequestDto
import com.example.data.ui.presentation.feature.orders.dto.request.UpdateStatusRequestDto
import com.example.domain.ui.presentation.feature.orders.model.CreateOrderRequest
import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.domain.ui.presentation.feature.orders.model.OrdersSummary
import com.example.domain.ui.presentation.feature.orders.model.PaymentMethod
import com.example.domain.ui.presentation.feature.orders.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val apiService: OrdersApiService
): OrdersRepository {

    override suspend fun getOrders(): Result<List<Order>> = runCatching {
        val response = apiService.getOrders()

        if (response.success) {
            val orders = response.orders?.map { it.toOrder() } ?: emptyList()
            Result.success(orders)
        } else {
            Result.failure(Exception("Не удалось загрузить заказы"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun getOrder(orderId: Long): Result<Order> = runCatching {
        val response = apiService.getOrder(orderId)

        if (response.success) {
            val order = response.order?.toOrder()
                ?: return@runCatching Result.failure(Exception("Заказ не найден"))
            Result.success(order)
        } else {
            Result.failure(Exception("Не удалось загрузить заказ"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun createOrder(request: CreateOrderRequest): Result<Order> = runCatching {
        val requestDto = CreateOrderRequestDto(
            contactInfo = ContactInfoDto(
                firstName = request.contactInfo.firstName,
                lastName = request.contactInfo.lastName,
                phone = request.contactInfo.phone,
                email = request.contactInfo.email
            ),
            address = AddressDto(
                country = request.address.country,
                city = request.address.city,
                street = request.address.street,
                postalCode = request.address.postalCode,
                apartment = request.address.apartment
            ),
            paymentMethod = when (request.paymentMethod) {
                PaymentMethod.CREDIT_CARD -> "credit_card"
                PaymentMethod.CASH_ON_DELIVERY -> "cash_on_delivery"
                PaymentMethod.PAYPAL -> "paypal"
                PaymentMethod.UNKNOWN -> "credit_card"
            }
        )

        val response = apiService.createOrder(requestDto)

        if (response.success) {
            val order = response.order?.toOrder()
                ?: return@runCatching Result.failure(Exception("Заказ не был создан"))
            Result.success(order)
        } else {
            Result.failure(Exception(response.message ?: "Не удалось создать заказ"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun updateOrderStatus(orderId: Long, status: OrderStatus): Result<Unit> = runCatching {
        val requestDto = UpdateStatusRequestDto(
            status = OrderStatus.toString(status)
        )

        val response = apiService.updateOrderStatus(orderId, requestDto)

        if (response.success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message ?: "Не удалось обновить статус заказа"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun deleteOrder(orderId: Long): Result<Unit> = runCatching {
        val response = apiService.deleteOrder(orderId)

        if (response.success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message ?: "Не удалось удалить заказ"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

    override suspend fun getOrdersSummary(): Result<OrdersSummary> = runCatching {
        val response = apiService.getOrders()

        if (response.success) {
            val summary = response.summary?.toOrdersSummary()
                ?: return@runCatching Result.failure(Exception("Нет данных по заказам"))
            Result.success(summary)
        } else {
            Result.failure(Exception("Не удалось загрузить статистику заказов"))
        }
    }.fold(
        onSuccess = { it },
        onFailure = { Result.failure(it) }
    )

}