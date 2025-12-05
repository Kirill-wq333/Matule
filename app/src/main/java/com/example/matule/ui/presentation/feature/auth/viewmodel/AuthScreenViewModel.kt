package com.example.matule.ui.presentation.feature.auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val tokenProvider: TokenProvider
): BaseViewModel<AuthScreenContract.Event, AuthScreenContract.State, AuthScreenContract.Effect>() {

    private val _authData = MutableStateFlow(AuthData())
    val authData = _authData.asStateFlow()

    override fun setInitialState(): AuthScreenContract.State = AuthScreenContract.State.Auth

    override fun handleEvent(event: AuthScreenContract.Event) = when (event) {
        is AuthScreenContract.Event.Login -> login(event.email, event.password)
        is AuthScreenContract.Event.CheckAuthStatus -> checkAuthStatus()
        is AuthScreenContract.Event.ClearError -> clearError()
    }

    init {
        checkExistingToken()
    }

    private fun checkExistingToken() {
        viewModelScope.launch(dispatcher) {
            authInteractor.getToken()
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch(dispatcher) {

            val result = authInteractor.login(email, password)

            when {
                result.isSuccess -> {
                    val user = result.getOrNull()!!

                    tokenProvider.getToken()

                    setState(AuthScreenContract.State.Success(user))
                    setEffect { AuthScreenContract.Effect.NavigateToMain }
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull()!!
                    setState(AuthScreenContract.State.Error(error.message ?: "Ошибка входа"))
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

    private fun checkAuthStatus() {
        viewModelScope.launch(dispatcher) {

            val isLoggedIn = authInteractor.isUserLoggedIn()
            if (isLoggedIn) {
                val user = authInteractor.getCurrentUser()
                if (user != null) {
                    setState ( AuthScreenContract.State.Success(user) )
                } else {
                    setState ( AuthScreenContract.State.Auth )
                }
            } else {
                setState ( AuthScreenContract.State.Auth )
            }
        }
    }

    private fun clearError() {
        _authData.value = _authData.value.copy(errorMessage = null)
        setState ( AuthScreenContract.State.Auth )
    }

    data class AuthData(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
}