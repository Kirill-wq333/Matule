package com.example.domain.ui.presentation.feature.notification.model

import kotlinx.serialization.Serializable

@Serializable
data class Notifications(
    val id: Long,
    val userId: Long,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: String,
){
    enum class NotificationType {
        ORDER, PROMOTION, SYSTEM, GENERAL;

        companion object {
            fun fromString(value: String?): NotificationType {
                return when (value?.lowercase()) {
                    "order" -> ORDER
                    "promotion" -> PROMOTION
                    "system" -> SYSTEM
                    else -> GENERAL
                }
            }
        }
    }

}

