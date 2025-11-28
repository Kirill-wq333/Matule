package com.example.data.ui.presentation.storage.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class AppPreferences(
    val isOnboardingCompleted: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userToken: String? = null,
    val userId: Long? = null
)

class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_ID = longPreferencesKey("user_id")
        val CART_ITEMS = stringSetPreferencesKey("cart_items")
    }

    val appPreferences: Flow<AppPreferences> = dataStore.data
        .map { preferences ->
            AppPreferences(
                isOnboardingCompleted = preferences[IS_ONBOARDING_COMPLETED] ?: false,
                isLoggedIn = preferences[IS_LOGGED_IN] ?: false,
                userToken = preferences[USER_TOKEN],
                userId = preferences[USER_ID]
            )
        }

    suspend fun setCartItems(productIds: Set<Long>) {
        val stringSet = productIds.map { it.toString() }.toSet()
        dataStore.edit { preferences ->
            preferences[CART_ITEMS] = stringSet
        }
    }

    suspend fun getCartItems(): Set<Long> {
        val stringSet = dataStore.data.first()[CART_ITEMS] ?: emptySet()
        val longSet = stringSet.mapNotNull { it.toLongOrNull() }.toSet()
        return longSet
    }

    suspend fun clearCartItems() {
        dataStore.edit { preferences ->
            preferences.remove(CART_ITEMS)
        }
    }

    suspend fun getAppPreferences(): AppPreferences {
        val prefs = dataStore.data.first()
        val onboarding = prefs[IS_ONBOARDING_COMPLETED] ?: false
        val loggedIn = prefs[IS_LOGGED_IN] ?: false
        val token = prefs[USER_TOKEN]

        return AppPreferences(
            isOnboardingCompleted = onboarding,
            isLoggedIn = loggedIn,
            userToken = token,
            userId = prefs[USER_ID]
        )
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = completed
        }
        val saved = dataStore.data.first()[IS_ONBOARDING_COMPLETED]
    }

    suspend fun setUserLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    fun getUserToken(): String? {
        return runBlocking {
            dataStore.data.first()[USER_TOKEN]
        }
    }

    suspend fun setUserToken(token: String?) {
        dataStore.edit { preferences ->
            if (token != null) {
                preferences[USER_TOKEN] = token
            } else {
                preferences.remove(USER_TOKEN)
            }
        }
    }

    suspend fun clearAllUserData() {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences.remove(USER_TOKEN)
            preferences.remove(USER_ID)
        }
    }
}