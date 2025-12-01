package com.example.data.ui.presentation.feature.notification

import com.example.data.ui.presentation.feature.notification.datasource.NotificationApiService
import com.example.domain.ui.presentation.feature.notification.model.Notifications
import com.example.domain.ui.presentation.feature.notification.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: NotificationApiService
): NotificationRepository {

    override suspend fun getNotifications(): Result<List<Notifications>> {
        return try {

            val notificationsDto = apiService.getNotifications()

            val notifications = notificationsDto.map { dto ->
                Notifications(
                    id = dto.id,
                    userId = dto.userId,
                    title = dto.title,
                    message = dto.message,
                    type = Notifications.NotificationType.fromString(dto.type),
                    isRead = dto.isRead,
                    createdAt = dto.createdAt
                )
            }


            Result.success(notifications)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}