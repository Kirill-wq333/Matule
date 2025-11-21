package com.example.matule.ui.presentation.feature.main.viewmodel

import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object MainScreenContract {
    sealed interface Event: ViewEvent {
        data object LoadContent: Event
        data object LoadingContent: Event
    }

    sealed interface State: ViewState {
        data object Arrivals: State
        data object Catalog: State
        data object Popular: State
        data object SideMenu: State
        data object Loaded: State
        data object Loading: State
    }
}