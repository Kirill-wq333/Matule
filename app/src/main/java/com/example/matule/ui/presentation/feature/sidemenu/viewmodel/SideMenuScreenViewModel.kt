package com.example.matule.ui.presentation.feature.sidemenu.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.profile.interactor.ProfileInteractor
import com.example.domain.ui.presentation.feature.profile.model.ProfileResult
import com.example.domain.ui.presentation.feature.sidemenu.interactor.SideMenuInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideMenuScreenViewModel @Inject constructor(
    private val sideMenuInteractor: SideMenuInteractor,
    private val profileInteractor: ProfileInteractor,
    private val tokenProvider: TokenProvider
) : BaseViewModel<SideMenuScreenContract.Event, SideMenuScreenContract.State, SideMenuScreenContract.Effect>() {

    override fun setInitialState(): SideMenuScreenContract.State = SideMenuScreenContract.State.Loading

    override fun handleEvent(event: SideMenuScreenContract.Event) = when (event) {
        is SideMenuScreenContract.Event.Logout -> logout()
        is SideMenuScreenContract.Event.LoadUserProfile -> loadProfile()
        is SideMenuScreenContract.Event.NavigateOnAuth -> navigateOnAuth()
    }

    init {
        viewModelScope.launch {
            loadProfile()
            tokenProvider.getToken()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            setState (SideMenuScreenContract.State.Loading )

            val result = profileInteractor.getProfile()
            if (result.isSuccess) {
                when (val profileResult = result.getOrNull()!!) {
                    is ProfileResult.Success -> {
                        setState (
                            SideMenuScreenContract.State.ProfileLoaded(
                                profile = profileResult.profile
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
        viewModelScope.launch {

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