package com.example.matule.ui.presentation.feature.favorite.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.domain.ui.presentation.feature.favorite.model.Favorite
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.favorite.viewmodel.FavoriteScreenContract
import com.example.matule.ui.presentation.feature.favorite.viewmodel.FavoriteScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.main.CardItem
import com.example.matule.ui.presentation.shared.screen.LoadingScreen
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

interface FavoriteScreenCallback{
    fun openDetailScreen(id: Long) {}
    fun addedInCart(id: Long) {}
    fun openFavoriteScreen() {}
    fun openCartScreen() {}
    fun removeFromFavorite(id: Long) {}
}


@Composable
fun FavoriteScreen(
    vm: FavoriteScreenViewModel,
    onBack: () -> Unit,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()

    val callback = object : FavoriteScreenCallback {
        override fun removeFromFavorite(id: Long) {
            vm.handleEvent(FavoriteScreenContract.Event.ToggleProductFavorite(id))
        }

        override fun openCartScreen() {
            navController.navigate(AppRouts.CART)
        }

        override fun openFavoriteScreen() {
            navController.navigate(AppRouts.POPULAR)
        }

        override fun addedInCart(id: Long) {
            vm.handleEvent(FavoriteScreenContract.Event.AddToCart(id))
        }

        override fun openDetailScreen(id: Long) {
            navController.navigate("${AppRouts.DETAILS}/$id")
        }
    }

    FavoriteContent(
        removeFromFavorite = { id ->
            callback.removeFromFavorite(id)
        },
        openDetailScreen = {
            callback.openDetailScreen(it)
        },
        openCartScreen = callback::openCartScreen,
        onBack = onBack,
        onLike = callback::openFavoriteScreen,
        state = state,
        addedInCart = { callback.addedInCart(it) }
    )
}

@Composable
private fun FavoriteContent(
    addedInCart: (Long) -> Unit,
    state: FavoriteScreenContract.State,
    onLike: () -> Unit,
    onBack: () -> Unit,
    removeFromFavorite: (Long) -> Unit,
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
            text = R.string.favourite,
            endIcon = R.drawable.ic_favorite_fill,
            onBack = onBack,
            tint = Colors.red,
            openScreen = onLike,
            visibleNameScreen = true,
            visibleEndIcon = true
        )
        when(state) {
            is FavoriteScreenContract.State.Loading -> {
                LoadingScreen()
            }
            is FavoriteScreenContract.State.Loaded -> {
                Content(
                    addedInCart = addedInCart,
                    openDetailScreen = openDetailScreen,
                    favorites = state.favorite,
                    cartItems = state.cartItems,
                    openCartScreen = openCartScreen,
                    removeFromFavorite = removeFromFavorite
                )
            }
            is FavoriteScreenContract.State.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_fill),
                            contentDescription = null,
                            tint = Colors.red,
                            modifier = Modifier.size(96.dp)
                        )
                        Text(
                            text = stringResource(R.string.empty_favorite),
                            color = Colors.text,
                            style = MatuleTypography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun Content(
    favorites: List<Favorite>,
    addedInCart: (Long) -> Unit,
    cartItems: Set<Long>,
    openDetailScreen: (Long) -> Unit,
    openCartScreen: () -> Unit,
    removeFromFavorite: (Long) -> Unit
) {
    val lazyVerticalGridState = rememberLazyGridState()

    val favoriteProduct = favorites.map { it.product }

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = lazyVerticalGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(
            items = favoriteProduct,
            key = { it.id }
        ) { favorite ->

            val isInCart = cartItems.contains(favorite.id)

            CardItem(
                cardName = favorite.name,
                cardImage = favorite.images.first(),
                money = favorite.price,
                addedInCart = {
                    addedInCart(favorite.id)
                },
                cartIcon = isInCart,
                liked = true,

                addedInFavorite = {
                    removeFromFavorite(favorite.id)
                },
                openCartScreen = openCartScreen,
                openDetailScreen = {
                    openDetailScreen(favorite.id)
                }
            )
        }
    }
}