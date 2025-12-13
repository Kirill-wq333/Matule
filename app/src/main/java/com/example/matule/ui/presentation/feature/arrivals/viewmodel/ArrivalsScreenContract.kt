package com.example.matule.ui.presentation.feature.arrivals.viewmodel

import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object ArrivalsScreenContract {
    sealed interface Event: ViewEvent{

        data object LoadedContent : Event

        data object RefreshContent : Event

    }
    sealed interface State: ViewState{

        data class Loaded(
            val promotions: List<Promotion> = emptyList()
        ) : State

        data object Loading : State

        data class Error(val message: String) : State
    }

    sealed interface Effect: ViewEffect{
        data class ShowError(val message: String) : Effect
    }
}