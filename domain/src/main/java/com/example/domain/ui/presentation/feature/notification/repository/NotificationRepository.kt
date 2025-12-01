package com.example.domain.ui.presentation.feature.notification.repository

import android.app.Notification
import com.example.domain.ui.presentation.feature.notification.model.Notifications

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notifications>>
}