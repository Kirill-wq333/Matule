package com.example.matule.ui.presentation.feature.auth.viewmodel

import com.example.domain.ui.presentation.feature.auth.model.User
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object AuthScreenContract {

    sealed interface State: ViewState {
        object Idle : State
        object Loading : State
        data class Success(val user: User) : State
        data class Error(val message: String) : State
    }
    sealed interface Event: ViewEvent {
        data class Login(val email: String, val password: String) : Event
        object Logout : Event
        object CheckAuthStatus : Event
    }
}