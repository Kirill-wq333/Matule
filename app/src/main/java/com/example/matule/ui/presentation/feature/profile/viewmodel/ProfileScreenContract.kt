package com.example.matule.ui.presentation.feature.profile.viewmodel

import com.example.domain.ui.presentation.feature.profile.model.UserProfile
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object ProfileScreenContract {

    sealed interface Event : ViewEvent {
        data object LoadProfile : Event
        data class UpdateProfileFields(
            val email: String,
            val firstName: String,
            val lastName: String? = null,
            val phone: String? = null,
            val country: String? = null,
            val city: String? = null,
            val address: String? = null,
            val postalCode: String? = null,
            val dateOfBirth: String? = null
        ) : Event
    }

    sealed interface State : ViewState {
        data object Loading : State
        data class ProfileLoaded(
            val isLoading: Boolean = false,
            val error: String? = null
        ) : State
        data class Error(val message: String) : State
    }

    sealed interface Effect : ViewEffect {
        data class ShowError(val message: String) : Effect
        data class ProfileUpdated(val profile: UserProfile) : Effect
    }

}