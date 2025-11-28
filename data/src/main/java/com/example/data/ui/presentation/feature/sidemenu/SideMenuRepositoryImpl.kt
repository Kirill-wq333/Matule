package com.example.data.ui.presentation.feature.sidemenu

import com.example.data.ui.presentation.feature.sidemenu.datasource.SideMenuApiService
import com.example.domain.ui.presentation.feature.sidemenu.repository.SideMenuRepository
import javax.inject.Inject

class SideMenuRepositoryImpl @Inject constructor(
    private val apiService: SideMenuApiService
): SideMenuRepository {

    override suspend fun logout(): Result<Boolean> {
        return try {
            val response = apiService.logout()
            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message ?: "Logout failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}