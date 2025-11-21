package com.example.matule.ui.presentation.feature.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
): BaseViewModel<AuthScreenContract.Event, AuthScreenContract.State, Nothing>() {

    private val _authData = MutableStateFlow(AuthData())
    val authData = _authData.asStateFlow()

    override fun setInitialState(): AuthScreenContract.State = AuthScreenContract.State.Loading

    override fun handleEvent(event: AuthScreenContract.Event) = when (event) {
        is AuthScreenContract.Event.Login -> login(event.email, event.password)
        is AuthScreenContract.Event.Logout -> logout()
        is AuthScreenContract.Event.CheckAuthStatus -> checkAuthStatus()
    }

    init {
        checkAuthStatus()
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            updateState { AuthScreenContract.State.Loading }
            _authData.value = _authData.value.copy(isLoading = true, errorMessage = null)

            val result = authInteractor.login(email, password)
            when {
                result.isSuccess -> {
                    val user = result.getOrNull()!!
                    updateState { AuthScreenContract.State.Success(user) }
                    _authData.value = _authData.value.copy(
                        isLoading = false,
                        email = "",
                        password = ""
                    )
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull()!!
                    updateState { AuthScreenContract.State.Error(error.message ?: "Ошибка входа") }
                    _authData.value = _authData.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Ошибка входа"
                    )
                }
            }
        }
    }

    fun updateEmail(email: String) {
        _authData.value = _authData.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _authData.value = _authData.value.copy(password = password)
    }

    private fun logout() {
        viewModelScope.launch {
            updateState { AuthScreenContract.State.Loading }
            authInteractor.logout()
            _authData.value = AuthData()
            updateState { AuthScreenContract.State.Idle }
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            updateState { AuthScreenContract.State.Loading }

            val isLoggedIn = authInteractor.isUserLoggedIn()
            if (isLoggedIn) {
                val user = authInteractor.getCurrentUser()
                if (user != null) {
                    updateState { AuthScreenContract.State.Success(user) }
                } else {
                    updateState { AuthScreenContract.State.Idle }
                }
            } else {
                updateState { AuthScreenContract.State.Idle }
            }
        }
    }

    data class AuthData(
        val email: String = "",
        val password: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val phone: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
}