package com.example.matule.ui.presentation.feature.popular.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.popular.viewmodel.PopularScreenContract
import com.example.matule.ui.presentation.feature.popular.viewmodel.PopularScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.main.CardItem
import com.example.matule.ui.presentation.shared.screen.EmptyScreen
import com.example.matule.ui.presentation.shared.screen.LoadingScreen
import com.example.matule.ui.presentation.theme.Colors

interface PopularScreenCallback{
    fun openDetailScreen(id: Long) {}
    fun addedInCart(id: Long) {}
    fun openFavoriteScreen() {}
    fun openCartScreen() {}
    fun addedInFavorite(id: Long, isFavorite: Boolean) {}
}

@Composable
fun PopularScreen(
    vm: PopularScreenViewModel,
    onBack: () -> Unit,
    navController: NavHostController
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(PopularScreenContract.Event.LoadedContent)
    }

    val callback = object : PopularScreenCallback {
        override fun addedInFavorite(id: Long, isFavorite: Boolean) {
            vm.handleEvent(PopularScreenContract.Event.ToggleProductFavorite(id, isFavorite))
        }

        override fun openCartScreen() {
            navController.navigate(AppRouts.CART)
        }

        override fun openFavoriteScreen() {
            navController.navigate(AppRouts.FAVOURITE)
        }

        override fun addedInCart(id: Long) {
            vm.handleEvent(PopularScreenContract.Event.AddToCart(id))
        }

        override fun openDetailScreen(id: Long) {
            navController.navigate("${AppRouts.DETAILS}/$id")
        }
    }

    PopularContent(
        onLike = callback::openFavoriteScreen,
        openCartScreen = callback::openCartScreen,
        onBack = onBack,
        openDetailScreen = {
            callback.openDetailScreen(it)
        },
        addedInCart = {
            callback.addedInCart(it)
        },
        addedInFavorite = { id, isFavorite ->
            callback.addedInFavorite(id, isFavorite)
        },
        state = state
    )
}

@Composable
private fun PopularContent(
    addedInCart: (Long) -> Unit,
    state: PopularScreenContract.State,
    onLike: () -> Unit,
    onBack: () -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Colors.background)
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomHeader(
            text = R.string.popular,
            endIcon = R.drawable.ic_favorite,
            onBack = onBack,
            openScreen = onLike,
            visibleNameScreen = true,
            visibleEndIcon = true
        )
        when(state) {
            is PopularScreenContract.State.Loading -> {
                LoadingScreen()
            }
            is PopularScreenContract.State.Loaded -> {
                Content(
                    addedInCart = addedInCart,
                    openDetailScreen = openDetailScreen,
                    populars = state.popularProducts,
                    cartItems = state.isEnableDot,
                    openCartScreen = openCartScreen,
                    addedInFavorite = addedInFavorite
                )
            }
            is PopularScreenContract.State.Empty -> {
                EmptyScreen(
                    modifier = Modifier.fillMaxSize(),
                    icon = R.drawable.ic_favorite,
                    emptyText = R.string.empty_popular
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun Content(
    populars: List<Product>,
    addedInCart: (Long) -> Unit,
    cartItems: Set<Long>,
    openDetailScreen: (Long) -> Unit,
    openCartScreen: () -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit
) {
    val lazyVerticalGridState = rememberLazyGridState()

    val popularProduct = populars.filter { it.isPopular }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = lazyVerticalGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(popularProduct) { popular ->

            val isInCart = cartItems.contains(popular.id)

            CardItem(
                cardName = popular.name,
                cardImage = popular.images.first(),
                money = popular.price,
                addedInCart = {
                    addedInCart(popular.id)
                },
                cartIcon = isInCart,
                liked = popular.isFavorite,

                addedInFavorite = {
                    addedInFavorite(popular.id,popular.isFavorite)
                },
                openCartScreen = openCartScreen,
                openDetailScreen = {
                    openDetailScreen(popular.id)
                }
            )
        }
    }
}