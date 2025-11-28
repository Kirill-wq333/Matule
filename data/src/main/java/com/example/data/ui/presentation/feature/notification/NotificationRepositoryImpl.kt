package com.example.data.ui.presentation.feature.notification

import com.example.data.ui.presentation.feature.notification.datasource.NotificationApiService
import com.example.domain.ui.presentation.feature.notification.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: NotificationApiService
): NotificationRepository {

}