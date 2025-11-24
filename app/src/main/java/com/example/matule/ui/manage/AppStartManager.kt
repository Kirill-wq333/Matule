package com.example.matule.ui.manage

import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.matule.ui.presentation.approuts.AppRouts
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStartManager @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val appPreferencesRepository: AppPreferencesImpl
) {
    suspend fun getStartDestination(): String {
        val appPreferences = appPreferencesRepository.getAppPreferences()
        val token = tokenProvider.getToken()

        return when {
            !appPreferences.isOnboardingCompleted -> {
                AppRouts.ONBOARDING
            }
            token != null && appPreferences.isLoggedIn -> {
                AppRouts.MAIN
            }
            else -> {
                AppRouts.AUTH
            }
        }
    }
}