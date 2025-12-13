package com.example.matule.ui.presentation.feature.arrivals.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.domain.ui.presentation.feature.arrivals.interactor.ArrivalsInteractor
import com.example.domain.ui.presentation.feature.auth.interactor.AuthInteractor
import com.example.domain.ui.presentation.feature.cart.interactor.CartInteractor
import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArrivalsScreenViewModel @Inject constructor(
    private val arrivalsInteractor: ArrivalsInteractor,
    private val cartInteractor: CartInteractor,
    private val authInteractor: AuthInteractor
) : BaseViewModel<ArrivalsScreenContract.Event, ArrivalsScreenContract.State, ArrivalsScreenContract.Effect>() {

    override fun setInitialState(): ArrivalsScreenContract.State = ArrivalsScreenContract.State.Loading

    override fun handleEvent(event: ArrivalsScreenContract.Event) = when (event) {
        is ArrivalsScreenContract.Event.RefreshContent -> refreshContent()
        is ArrivalsScreenContract.Event.LoadedContent -> loadArrivals()
    }

    init {
        checkAuthAndLoadContent()
    }

    private fun checkAuthAndLoadContent() {
        viewModelScope.launch(dispatcher) {
            val isLoggedIn = authInteractor.isUserLoggedIn()

            if (isLoggedIn) {
                cartInteractor.getLocalCartItems()
                loadArrivals()
            }
        }
    }


    private fun loadArrivals() {
        viewModelScope.launch(dispatcher) {
            setState(ArrivalsScreenContract.State.Loading)

            val result = arrivalsInteractor.loadPromotions()

            if (result.isSuccess) {
                val content = result.getOrNull()!!

                setState(
                    ArrivalsScreenContract.State.Loaded(
                        promotions = content
                    )
                )

            } else {
                val error = result.exceptionOrNull()!!
                setState (ArrivalsScreenContract.State.Error(error.message ?: "Ошибка загрузки данных") )
                setEffect { ArrivalsScreenContract.Effect.ShowError("Не удалось загрузить данные") }
            }
        }
    }

    private fun refreshContent(){
        setState(ArrivalsScreenContract.State.Loading)
    }
}