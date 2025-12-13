package com.example.matule.ui.presentation.feature.popular.viewmodel

import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object PopularScreenContract {

    sealed interface Event: ViewEvent{
        data class ToggleProductFavorite(val productId: Long, val currentlyFavorite: Boolean) : Event
        data object LoadedContent: Event
        data object RefreshContent : Event
        data class AddToCart(val productId: Long, val quantity: Int = 1) : Event
    }

    sealed interface State: ViewState{
        data class Loaded(
            val popularProducts: List<Product> = emptyList(),
            val cartState: CartState = CartState(),
            val isEnableDot: Set<Long> = emptySet()
        ): State
        data object Loading: State
        data object Empty: State
        data class Error(val message: String) : State
    }

    sealed interface Effect: ViewEffect{
        data class FavoriteStatusUpdated(val result: FavoriteResult) : Effect
        data class ShowError(val message: String) : Effect
        data class CartItemAdded(val productId: Long) : Effect
    }
}