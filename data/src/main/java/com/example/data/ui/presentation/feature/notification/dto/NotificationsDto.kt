package com.example.data.ui.presentation.feature.notification.dto


data class NotificationsDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: String,
)
