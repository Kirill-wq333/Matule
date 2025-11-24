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

        println("🔍 AppStartManager - Token: $token")
        println("🔍 AppStartManager - Onboarding completed: ${appPreferences.isOnboardingCompleted}")
        println("🔍 AppStartManager - Is logged in: ${appPreferences.isLoggedIn}")

        return when {
            !appPreferences.isOnboardingCompleted -> {
                println("🚀 Start: ONBOARDING (first launch)")
                AppRouts.ONBOARDING
            }
            token != null && appPreferences.isLoggedIn -> {
                println("🚀 Start: MAIN (user logged in)")
                AppRouts.MAIN
            }
            else -> {
                println("🚀 Start: AUTH (user not logged in)")
                AppRouts.AUTH
            }
        }
    }
}