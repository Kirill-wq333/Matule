package com.example.matule.ui.presentation.feature.details.viewmodel

import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState
import com.example.matule.ui.presentation.feature.main.viewmodel.MainScreenContract

object ProductDetailContract {

    sealed interface Event : ViewEvent{
        data class LoadContent(
            val productId : Long
        ) : Event
        data object Refresh : Event
        data class ToggleProductFavorite(val productId: Long, val currentlyFavorite: Boolean) : Event
        data class AddToCart(val productId: Long, val quantity: Int = 1) : Event
    }
    sealed interface State : ViewState{
        data class LoadProduct(
            val product: Product,
            val cartState: CartState = CartState(),
            val isEnableDot: Set<Long> = emptySet()
        ) : State
        data object Loading : State
    }

    sealed interface Effect : ViewEffect{
        data class CartItemRemoved(val id: Long) : Effect
        data class ShowError(val message: String) : Effect
        data class FavoriteStatusUpdated(val result: FavoriteResult) : Effect
        data class CartItemAdded(val productId: Long) : Effect
    }
}