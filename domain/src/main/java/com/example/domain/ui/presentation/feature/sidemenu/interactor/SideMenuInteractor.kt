package com.example.domain.ui.presentation.feature.sidemenu.interactor

import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.sidemenu.repository.SideMenuRepository

class SideMenuInteractor(
    private val sideMenuRepository: SideMenuRepository,
    private val authInteractor: AuthInteractor,

) {
    suspend fun logout(): Result<Boolean> {
        return try {
            val apiResult = sideMenuRepository.logout()

            clearAllLocalData()

            Result.success(true)
        } catch (e: Exception) {
            clearAllLocalData()
            Result.success(true)
        }
    }

    private suspend fun clearAllLocalData() {
        authInteractor.logout()
    }
}