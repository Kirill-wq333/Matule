package com.example.matule.ui.presentation.feature.notification.viewmodel

import com.example.domain.ui.presentation.feature.notification.model.Notifications
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object NotificationScreenContract {

    sealed interface Event : ViewEvent {
        data object LoadNotifications : Event
        data object RefreshNotifications : Event
    }

    sealed interface State : ViewState {
        data object Loading : State
        data object Empty : State
        data class Loaded(val notifications: List<Notifications>) : State
        data class Error(val message: String) : State
    }

    sealed interface Effect : ViewEffect {
        data class ShowError(val message: String) : Effect
    }

}