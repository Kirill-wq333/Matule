package com.example.domain.ui.presentation.feature.profile.model

sealed class ProfileResult {
    data class Success(val profile: UserProfile) : ProfileResult()
    data class Error(val message: String) : ProfileResult()
}