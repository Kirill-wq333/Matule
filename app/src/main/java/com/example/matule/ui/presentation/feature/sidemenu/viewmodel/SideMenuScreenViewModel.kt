package com.example.matule.ui.presentation.feature.sidemenu.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.notification.interactor.NotificationInteractor
import com.example.domain.ui.presentation.feature.notification.model.Notifications
import com.example.domain.ui.presentation.feature.profile.interactor.ProfileInteractor
import com.example.domain.ui.presentation.feature.profile.model.ProfileResult
import com.example.domain.ui.presentation.feature.sidemenu.interactor.SideMenuInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideMenuScreenViewModel @Inject constructor(
    private val sideMenuInteractor: SideMenuInteractor,
    private val profileInteractor: ProfileInteractor,
    private val tokenProvider: TokenProvider,
    private val notificationInteractor: NotificationInteractor
) : BaseViewModel<SideMenuScreenContract.Event, SideMenuScreenContract.State, SideMenuScreenContract.Effect>() {

    private val _notifications: MutableStateFlow<List<Notifications>> = MutableStateFlow(emptyList())
    val notifications = _notifications.asStateFlow()

    override fun setInitialState(): SideMenuScreenContract.State = SideMenuScreenContract.State.Loading

    override fun handleEvent(event: SideMenuScreenContract.Event) = when (event) {
        is SideMenuScreenContract.Event.Logout -> logout()
        is SideMenuScreenContract.Event.LoadUserProfile -> loadProfile()
        is SideMenuScreenContract.Event.NavigateOnAuth -> navigateOnAuth()
    }

    init {
        viewModelScope.launch(dispatcher) {
            loadProfile()
            tokenProvider.getToken()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch(dispatcher) {
            setState (SideMenuScreenContract.State.Loading )

            val result = profileInteractor.getProfile()
            val resultNotification = notificationInteractor.getNotifications()
            if (result.isSuccess && resultNotification.isSuccess) {
                when (val profileResult = result.getOrNull()!!) {
                    is ProfileResult.Success -> {
                        setState (
                            SideMenuScreenContract.State.ProfileLoaded(
                                profile = profileResult.profile,
                                notification = resultNotification.getOrNull()!!
                            )
                        )
                    }
                    is ProfileResult.Error -> {
                        setState (SideMenuScreenContract.State.Error(profileResult.message) )
                        setEffect { SideMenuScreenContract.Effect.ShowError(profileResult.message) }
                    }
                }
            } else {
                setState (SideMenuScreenContract.State.Error("Ошибка загрузки профиля") )
                setEffect { SideMenuScreenContract.Effect.ShowError("Ошибка загрузки профиля") }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch(dispatcher) {

            val result = sideMenuInteractor.logout()

            if (result.isSuccess) {
                setEffect { SideMenuScreenContract.Effect.NavigateOnAuth }
                setEffect { SideMenuScreenContract.Effect.ShowMessage("Logged out successfully") }
            } else {
                setState(SideMenuScreenContract.State.Error("Logout failed"))
                setEffect { SideMenuScreenContract.Effect.ShowError("Logout failed. Please try again.") }
            }
        }
    }

    private fun navigateOnAuth(){
        setEffect { SideMenuScreenContract.Effect.NavigateOnAuth }
    }
}