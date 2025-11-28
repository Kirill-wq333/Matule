package com.example.matule.ui.presentation.feature.register.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.data.ui.presentation.storage.tokenprovider.TokenProvider
import com.example.domain.ui.presentation.feature.register.interactor.RegisterInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerInteractor: RegisterInteractor,
    private val tokenProvider: TokenProvider
) : BaseViewModel<RegisterContract.Event, RegisterContract.State, Nothing>() {

    override fun setInitialState(): RegisterContract.State = RegisterContract.State.Loaded

    override fun handleEvent(event: RegisterContract.Event) = when(event) {
        is RegisterContract.Event.Register -> register(event.email, event.password, event.firstName, event.lastName, event.phone)
    }

    private fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String? = null,
        phone: String? = null
    ){
        viewModelScope.launch {
            val registerUser = registerInteractor.register(email,password,firstName,lastName,phone)

            when {
                registerUser.isSuccess -> {
                    registerUser.getOrNull()!!
                    tokenProvider.getToken()
                    setState(RegisterContract.State.Loaded)
                }
                registerUser.isFailure ->{
                    registerUser.exceptionOrNull()!!
                }
            }
        }
    }
}