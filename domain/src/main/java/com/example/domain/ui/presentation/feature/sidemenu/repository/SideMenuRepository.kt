package com.example.domain.ui.presentation.feature.sidemenu.repository

interface SideMenuRepository {
    suspend fun logout(): Result<Boolean>
}