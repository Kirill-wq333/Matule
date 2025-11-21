package com.example.matule.ui.presentation.feature.main.viewmodel

import com.example.matule.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
//    private  val mainInteractor: MainInteractor
): BaseViewModel<MainScreenContract.Event, MainScreenContract.State, Nothing>() {

    override fun setInitialState(): MainScreenContract.State = MainScreenContract.State.Loaded

    override fun handleEvent(event: MainScreenContract.Event) = when(event) {
        is MainScreenContract.Event.LoadContent ->{}
        is MainScreenContract.Event.LoadingContent -> {}
    }


}