package com.example.matule.ui.presentation.feature.main.ui.screenscomponents

data class SideMenu(
    val route: String,
    val icon: Int,
    val label: Int,
    val notificationItem: Int = 0
)

data class SideMenuExit(
    val route: Boolean,
    val icon: Int,
    val label: Int,
)
