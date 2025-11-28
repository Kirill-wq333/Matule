package com.example.matule.ui.presentation.feature.register.viewmodel

import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object RegisterContract {

    sealed interface Event: ViewEvent{
        data class Register(
            val email: String,
            val password: String,
            val firstName: String,
            val lastName: String? = null,
            val phone: String? = null
        ) : Event
    }

    sealed interface State: ViewState{
        data object Loaded: State
    }
}