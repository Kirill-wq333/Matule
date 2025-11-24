package com.example.matule.ui.presentation.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.preferences.AppPreferencesImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appPreferences: AppPreferencesImpl
) : ViewModel() {
    
    private val _onOnboardingCompleted = MutableSharedFlow<Boolean>()
    val onOnboardingCompleted: SharedFlow<Boolean> = _onOnboardingCompleted.asSharedFlow()

    fun completeOnboarding() {
        viewModelScope.launch {
            appPreferences.setOnboardingCompleted(true)
            val savedPrefs = appPreferences.getAppPreferences()
            _onOnboardingCompleted.emit(true)
        }
    }

    fun getToken(): String? {
        return runBlocking {
            appPreferences.getAppPreferences().userToken
        }
    }
}