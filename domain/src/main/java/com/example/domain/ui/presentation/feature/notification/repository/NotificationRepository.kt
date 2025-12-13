package com.example.domain.ui.presentation.feature.notification.repository

import com.example.domain.ui.presentation.feature.notification.model.Notifications

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notifications>>
}