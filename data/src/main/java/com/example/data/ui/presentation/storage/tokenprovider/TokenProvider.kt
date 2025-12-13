package com.example.data.ui.presentation.storage.tokenprovider

import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenProvider @Inject constructor(
    private val appPreferences: AppPreferencesImpl
) {

    fun getToken(): String? {
        val token = appPreferences.getUserToken()
        return token?.ifEmpty { null }
    }

    fun saveToken(token: String) {
        runBlocking {
            appPreferences.setUserToken(token)
            appPreferences.setUserLoggedIn(true)
        }

        getToken()
    }

//    fun clearToken() {
//        runBlocking {
//            appPreferences.setUserToken(null)
//            appPreferences.setUserLoggedIn(false)
//        }
//    }

    fun isUserLoggedIn(): Boolean {
        return runBlocking {
            appPreferences.getAppPreferences().isLoggedIn
        }
    }
}