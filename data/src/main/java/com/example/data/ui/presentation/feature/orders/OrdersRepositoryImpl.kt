package com.example.data.ui.presentation.feature.orders

import com.example.data.ui.presentation.feature.orders.datasource.OrdersApiService
import com.example.domain.ui.presentation.feature.orders.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val apiService: OrdersApiService
): OrdersRepository {

}