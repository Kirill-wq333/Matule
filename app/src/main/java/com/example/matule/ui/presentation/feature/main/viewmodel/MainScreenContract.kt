package com.example.matule.ui.presentation.feature.main.viewmodel

import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.domain.ui.presentation.feature.catalog.model.Category
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.domain.ui.presentation.feature.favorite.model.FavoriteResult
import com.example.matule.ui.core.viewmodel.ViewEffect
import com.example.matule.ui.core.viewmodel.ViewEvent
import com.example.matule.ui.core.viewmodel.ViewState

object MainScreenContract {

    sealed interface Event : ViewEvent {
        data object LoadContent : Event
        data class ToggleProductFavorite(val productId: Long, val currentlyFavorite: Boolean) : Event
        data class SelectCategory(val categoryId: Long) : Event
        data class LoadProductsByCategory(val category: String) : Event
        data object RefreshContent : Event
        data class AddToCart(val productId: Long, val quantity: Int = 1) : Event
    }

    sealed interface State : ViewState {
        data object Loading : State
        data class Loaded(
            val categories: List<Category> = emptyList(),
            val popularProducts: List<Product> = emptyList(),
            val promotions: List<Promotion> = emptyList(),
            val selectedCategoryId: Long? = null,
            val cartState: CartState = CartState(),
            val isEnableDot: Set<Long> = emptySet()
        ) : State
        data class Error(val message: String) : State

    }

    sealed interface Effect : ViewEffect {
        data class ShowError(val message: String) : Effect
        data class CartItemRemoved(val id: Long) : Effect
        data class FavoriteStatusUpdated(val result: FavoriteResult) : Effect
        data class CategoryProductsLoaded(val category: String, val products: List<Product>) : Effect
        data class CartItemAdded(val productId: Long) : Effect
    }
}