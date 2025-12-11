package com.example.data.ui.presentation.feature.orders.dto.request

import com.example.data.ui.presentation.feature.orders.dto.AddressDto
import com.example.data.ui.presentation.feature.orders.dto.ContactInfoDto

data class CreateOrderRequestDto(
    val contactInfo: ContactInfoDto,
    val address: AddressDto,
    val paymentMethod: String
)

data class UpdateStatusRequestDto(
    val status: String
)