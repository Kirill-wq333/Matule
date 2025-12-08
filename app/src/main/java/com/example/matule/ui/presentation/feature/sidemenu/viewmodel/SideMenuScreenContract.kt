package com.example.matule.ui.presentation.feature.sidemenu.viewmodel

import com.example.domain.ui.presentation.feature.notification.model.Notifications
import com.example.domain.ui.presentation.feature.profile.model.UserProfile
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenContract

object SideMenuScreenContract {
    sealed interface Event: ViewEvent{
        data object Logout : Event
        data object LoadUserProfile : Event
        data object NavigateOnAuth : Event
    }

    sealed interface State : ViewState{
        data object Loading: State
        data class ProfileLoaded(
            val profile: UserProfile,
            val notification: List<Notifications>
        ) : State
        data class Error(val message: String) : State
    }

    sealed interface Effect: ViewEffect{
        data object NavigateOnAuth: Effect
        data class ShowError(val message: String) : Effect
        data class ShowMessage(val message: String) : Effect
    }
}