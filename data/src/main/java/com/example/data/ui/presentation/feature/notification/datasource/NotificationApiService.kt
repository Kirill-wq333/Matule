package com.example.data.ui.presentation.feature.notification.datasource

import com.example.data.ui.presentation.feature.notification.dto.NotificationsDto
import com.example.data.ui.presentation.network.annotation.WithAuthorization
import retrofit2.http.GET

interface NotificationApiService {

    @WithAuthorization
    @GET("notifications")
    suspend fun getNotifications(): List<NotificationsDto>

}