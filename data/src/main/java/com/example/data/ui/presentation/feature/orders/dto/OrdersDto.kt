package com.example.data.ui.presentation.feature.orders.dto

import com.example.data.ui.presentation.feature.orders.dto.AddressDto.Companion.toAddress
import com.example.data.ui.presentation.feature.orders.dto.ContactInfoDto.Companion.toContactInfo
import com.example.data.ui.presentation.feature.orders.dto.OrderItemDto.Companion.toOrderItem
import com.example.domain.ui.presentation.feature.orders.model.Address
import com.example.domain.ui.presentation.feature.orders.model.ContactInfo
import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.domain.ui.presentation.feature.orders.model.OrderItem
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.domain.ui.presentation.feature.orders.model.OrdersSummary
import com.example.domain.ui.presentation.feature.orders.model.PaymentMethod

data class OrderDto(
    val id: Long,
    val orderNumber: Int,
    val userId: Long,
    val items: List<OrderItemDto>,
    val subtotal: Double,
    val delivery: Double,
    val total: Double,
    val status: String,
    val contactInfo: ContactInfoDto,
    val address: AddressDto,
    val paymentMethod: String,
    val createdAt: String,
    val updatedAt: String
){
    companion object{
        fun OrderDto.toOrder(): Order =
            Order(
                id = id,
                orderNumber = orderNumber,
                userId = userId,
                items = items.map { it.toOrderItem() },
                subtotal = subtotal,
                delivery = delivery,
                total = total,
                status = OrderStatus.fromString(status),
                contactInfo = contactInfo.toContactInfo(),
                address = address.toAddress(),
                paymentMethod = PaymentMethod.fromString(paymentMethod),
                createdAt = createdAt,
                updatedAt = updatedAt
            )

    }
}

data class OrderItemDto(
    val productId: Long,
    val name: String,
    val price: Double,
    val quantity: Int,
    val image: String
){
    companion object{
        fun OrderItemDto.toOrderItem(): OrderItem =
            OrderItem(
                productId = productId,
                name = name,
                price = price,
                quantity = quantity,
                image = image
            )

    }
}

data class ContactInfoDto(
    val firstName: String,
    val lastName: String?,
    val phone: String,
    val email: String?
){
    companion object{
        fun ContactInfoDto.toContactInfo(): ContactInfo =
            ContactInfo(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                email = email
            )
    }
}

data class AddressDto(
    val country: String,
    val city: String,
    val street: String,
    val postalCode: String?,
    val apartment: String?
){
    companion object{
        fun AddressDto.toAddress(): Address =
            Address(
                country = country,
                city = city,
                street = street,
                postalCode = postalCode,
                apartment = apartment
            )
    }
}

data class OrdersSummaryDto(
    val totalOrders: Int,
    val delivered: Int,
    val processing: Int
){
    companion object{
        fun OrdersSummaryDto.toOrdersSummary(): OrdersSummary =
            OrdersSummary(
                totalOrders = totalOrders,
                delivered = delivered,
                processing = processing
            )
    }
}