package com.example.matule.ui.presentation.feature.profile.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.profile.interactor.ProfileInteractor
import com.example.domain.ui.presentation.feature.profile.model.ProfileResult
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val tokenProvider: TokenProvider
) : BaseViewModel<ProfileScreenContract.Event, ProfileScreenContract.State, ProfileScreenContract.Effect>() {

    override fun setInitialState(): ProfileScreenContract.State = ProfileScreenContract.State.Loading

    override fun handleEvent(event: ProfileScreenContract.Event) = when (event) {
        is ProfileScreenContract.Event.LoadProfile -> loadProfile()
        is ProfileScreenContract.Event.UpdateProfileFields -> updateProfileFields(
            event.email,
            event.firstName,
            event.lastName,
            event.phone,
            event.country,
            event.city,
            event.address,
            event.postalCode,
            event.dateOfBirth
        )
    }

    private fun loadProfile() {
        viewModelScope.launch {
            setState ( ProfileScreenContract.State.Loading )

            val result = profileInteractor.getProfile()
            if (result.isSuccess) {
                val profileResult = result.getOrNull()!!
                when (profileResult) {
                    is ProfileResult.Success -> {
                        setState (
                            ProfileScreenContract.State.ProfileLoaded(
                                profile = profileResult.profile
                            )
                        )
                    }
                    is ProfileResult.Error -> {
                        setState ( ProfileScreenContract.State.Error(profileResult.message) )
                        setEffect { ProfileScreenContract.Effect.ShowError(profileResult.message) }
                    }
                }
            } else {
                setState ( ProfileScreenContract.State.Error("Ошибка загрузки профиля") )
                setEffect { ProfileScreenContract.Effect.ShowError("Ошибка загрузки профиля") }
            }
        }
    }

    private fun updateProfileFields(
        email: String,
        firstName: String,
        lastName: String?,
        phone: String?,
        country: String?,
        city: String?,
        address: String?,
        postalCode: String?,
        dateOfBirth: String?
    ) {
        viewModelScope.launch {

            setState (
                when (val currentState = currentState) {
                    is ProfileScreenContract.State.ProfileLoaded ->
                        currentState.copy(isLoading = true, error = null)
                    else -> ProfileScreenContract.State.Loading
                }
            )

            val result = profileInteractor.updateProfileFields(
                email = email,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                country = country,
                city = city,
                address = address,
                postalCode = postalCode,
                dateOfBirth = dateOfBirth
            )

            tokenProvider.getToken()

            if (result.isSuccess) {
                val profileResult = result.getOrNull()!!
                when (profileResult) {
                    is ProfileResult.Success -> {
                        setState(
                            ProfileScreenContract.State.ProfileLoaded(
                                profile = profileResult.profile
                            )
                        )
                        setEffect { ProfileScreenContract.Effect.ProfileUpdated(profileResult.profile) }
                    }

                    is ProfileResult.Error -> {
                        setState(
                            when (val currentState = currentState) {
                                is ProfileScreenContract.State.ProfileLoaded ->
                                    currentState.copy(
                                        isLoading = false,
                                        error = profileResult.message
                                    )

                                else -> ProfileScreenContract.State.Error(profileResult.message)
                            }
                        )
                        setEffect { ProfileScreenContract.Effect.ShowError(profileResult.message) }
                    }
                }
            } else {
                setEffect { ProfileScreenContract.Effect.ShowError("Ошибка обновления профиля") }
            }
        }
    }

    init {
        viewModelScope.launch {
            loadProfile()
            tokenProvider.getToken()
        }
    }
}