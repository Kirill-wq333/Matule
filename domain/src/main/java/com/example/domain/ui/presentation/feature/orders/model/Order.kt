package com.example.domain.ui.presentation.feature.orders.model

import kotlinx.serialization.Serializable
@Serializable
data class Order(
    val id: Long,
    val orderNumber: Int,
    val userId: Long,
    val items: List<OrderItem>,
    val subtotal: Double,
    val delivery: Double,
    val total: Double,
    val status: OrderStatus,
    val contactInfo: ContactInfo,
    val address: Address,
    val paymentMethod: PaymentMethod,
    val createdAt: String,
    val updatedAt: String
)

data class OrderItem(
    val productId: Long,
    val name: String,
    val price: Double,
    val quantity: Int,
    val image: String
)

data class ContactInfo(
    val firstName: String,
    val lastName: String?,
    val phone: String,
    val email: String?
)

data class Address(
    val country: String,
    val city: String,
    val street: String,
    val postalCode: String?,
    val apartment: String?
)

enum class OrderStatus {
    PROCESSING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    UNKNOWN;

    companion object {
        fun fromString(status: String): OrderStatus {
            return when (status.lowercase()) {
                "processing" -> PROCESSING
                "confirmed" -> CONFIRMED
                "shipped" -> SHIPPED
                "delivered" -> DELIVERED
                "cancelled" -> CANCELLED
                else -> UNKNOWN
            }
        }

        fun toString(status: OrderStatus): String {
            return when (status) {
                PROCESSING -> "processing"
                CONFIRMED -> "confirmed"
                SHIPPED -> "shipped"
                DELIVERED -> "delivered"
                CANCELLED -> "cancelled"
                UNKNOWN -> "unknown"
            }
        }
    }
}

enum class PaymentMethod {
    CREDIT_CARD,
    CASH_ON_DELIVERY,
    PAYPAL,
    UNKNOWN;

    companion object {
        fun fromString(method: String): PaymentMethod {
            return when (method.lowercase()) {
                "credit_card", "credit card" -> CREDIT_CARD
                "cash_on_delivery", "cash" -> CASH_ON_DELIVERY
                "paypal" -> PAYPAL
                else -> UNKNOWN
            }
        }
    }
}

data class OrdersSummary(
    val totalOrders: Int,
    val delivered: Int,
    val processing: Int
)