package com.example.matule.ui.presentation.feature.auth.viewmodel

import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object AuthScreenContract {

    sealed interface Event : ViewEvent {
        data class Login(val email: String, val password: String) : Event
        data object CheckAuthStatus : Event
        data object ClearError : Event
    }

    sealed interface State : ViewState {
        data object Auth: State
        data class Success(val user: User) : State
        data class Error(val message: String) : State
    }

    sealed interface Effect : ViewEffect {
        data object NavigateToMain : Effect
        data object NavigateToLogin : Effect
        data class ShowError(val message: String) : Effect
    }
}