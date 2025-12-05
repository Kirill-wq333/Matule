package com.example.matule.ui.presentation.feature.notification.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.notification.interactor.NotificationInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationScreenViewModel @Inject constructor(
    private val notificationInteractor: NotificationInteractor,
    private val authInteractor: AuthInteractor
) : BaseViewModel<NotificationScreenContract.Event, NotificationScreenContract.State, NotificationScreenContract.Effect>() {

    override fun setInitialState(): NotificationScreenContract.State = NotificationScreenContract.State.Loading

    override fun handleEvent(event: NotificationScreenContract.Event) = when(event){
        is NotificationScreenContract.Event.LoadNotifications -> loadNotifications()
        is NotificationScreenContract.Event.RefreshNotifications -> refreshNotifications()
    }

    init {
        handleEvent(NotificationScreenContract.Event.LoadNotifications)
    }

    private fun loadNotifications() {
        viewModelScope.launch(dispatcher) {
            setState(NotificationScreenContract.State.Loading)

            val result = notificationInteractor.getNotifications()
            authInteractor.isUserLoggedIn()
            if (result.isSuccess) {
                val notifications = result.getOrNull()!!

                if (notifications.isEmpty()) {
                    setState(NotificationScreenContract.State.Empty)
                } else {
                    setState(NotificationScreenContract.State.Loaded(notifications))
                }
            } else {
                val error = result.exceptionOrNull()!!
                val errorMessage = error.message ?: "Ошибка загрузки"
                setState(NotificationScreenContract.State.Error(errorMessage))
                setEffect {
                    NotificationScreenContract.Effect.ShowError("Не удалось загрузить уведомления: $errorMessage")
                }
            }
        }
    }

    private fun refreshNotifications() {
        loadNotifications()
    }
}