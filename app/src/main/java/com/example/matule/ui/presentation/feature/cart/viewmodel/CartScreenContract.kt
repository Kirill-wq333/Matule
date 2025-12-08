package com.example.matule.ui.presentation.feature.cart.viewmodel

import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object CartScreenContract {

    sealed interface Event: ViewEvent{
        data object LoadCart : Event
        data class UpdateQuantity(val cartItemId: Long, val newQuantity: Int) : Event
        data class RemoveItem(val cartItemId: Long) : Event
    }

    sealed interface State: ViewState{
        data class Loaded(
            val items: List<CartItem> = emptyList(),
            val totalPrice: Double = 0.0,
            val subtotal: Double = 0.0,
            val delivery: Double = 0.0,
        ): State
        data object Empty: State
        data object Loading: State
    }

    sealed interface Effect: ViewEffect{
        data class ShowSnackbar(val message: String) : Effect
        data class ShowToast(val message: String) : Effect
    }

}