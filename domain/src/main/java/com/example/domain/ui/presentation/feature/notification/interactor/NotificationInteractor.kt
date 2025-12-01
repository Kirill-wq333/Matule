package com.example.domain.ui.presentation.feature.notification.interactor

import com.example.domain.ui.presentation.feature.notification.model.Notifications
import com.example.domain.ui.presentation.feature.notification.repository.NotificationRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository
) {

    suspend fun getNotifications(): Result<List<Notifications>> {
        return notificationRepository.getNotifications()
    }

}