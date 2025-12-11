package com.example.data.ui.presentation.feature.orders.datasource

import com.example.data.ui.presentation.feature.orders.dto.request.CreateOrderRequestDto
import com.example.data.ui.presentation.feature.orders.dto.request.UpdateStatusRequestDto
import com.example.data.ui.presentation.feature.orders.dto.response.CreateOrderResponseDto
import com.example.data.ui.presentation.feature.orders.dto.response.DeleteOrderResponseDto
import com.example.data.ui.presentation.feature.orders.dto.response.OrderResponseDto
import com.example.data.ui.presentation.feature.orders.dto.response.OrdersResponseDto
import com.example.data.ui.presentation.feature.orders.dto.response.UpdateStatusResponseDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrdersApiService {

    @WithAuthorization
    @GET("orders")
    suspend fun getOrders(): OrdersResponseDto

    @WithAuthorization
    @GET("orders/{id}")
    suspend fun getOrder(@Path("id") orderId: Long): OrderResponseDto

    @WithAuthorization
    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequestDto): CreateOrderResponseDto

    @WithAuthorization
    @PUT("orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") orderId: Long,
        @Body request: UpdateStatusRequestDto
    ): UpdateStatusResponseDto

    @WithAuthorization
    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") orderId: Long): DeleteOrderResponseDto

}