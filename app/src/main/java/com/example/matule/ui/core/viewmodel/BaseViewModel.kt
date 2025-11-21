package com.example.matule.ui.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewEffect

interface ViewState

interface ViewEvent

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewEffect>
    (protected val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    protected val currentState: UiState
        get() = state.value

    private val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val state = _viewState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch(dispatcher) {
            event.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun handleEvent(event: Event)


    fun setEvent(event: Event) {
        viewModelScope.launch(dispatcher) { _event.emit(event) }
    }

    protected fun setState(newState: UiState) {
        _viewState.value = newState
    }

    protected fun updateState(reducer: UiState.() -> UiState) {
        val newState = state.value.reducer()
        _viewState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch(dispatcher) { _effect.send(effectValue) }
    }


}