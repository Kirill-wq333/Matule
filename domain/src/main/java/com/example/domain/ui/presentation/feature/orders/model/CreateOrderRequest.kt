package com.example.domain.ui.presentation.feature.orders.model

data class CreateOrderRequest(
    val contactInfo: ContactInfo,
    val address: Address,
    val paymentMethod: PaymentMethod
)