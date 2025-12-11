package com.example.domain.ui.presentation.feature.orders.model

import com.example.domain.ui.presentation.feature.orders.model.Address
import com.example.domain.ui.presentation.feature.orders.model.ContactInfo
import com.example.domain.ui.presentation.feature.orders.model.PaymentMethod

data class CreateOrderRequest(
    val contactInfo: ContactInfo,
    val address: Address,
    val paymentMethod: PaymentMethod
)