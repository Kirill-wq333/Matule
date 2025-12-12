package com.example.matule.ui.presentation.shared.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.R
import com.example.matule.ui.presentation.feature.main.ui.Card
import com.example.matule.ui.presentation.shared.screen.EmptyContent

@Composable
fun PopularCard(
    openPopularScreen: () -> Unit,
    cartItems: Set<Long>,
    popularProducts: List<Product>,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit = {},
    openDetailScreen: (Long) -> Unit = {}
) {
    val products = popularProducts.take(2)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val density = LocalDensity.current

    val hasEnoughSpace = remember(products.size, screenWidth) {
        with(density) {
            val itemWidth = 160.dp.toPx() * products.size
            val totalWidth = screenWidth.toPx() - (40.dp.toPx())
            totalWidth - itemWidth > 15.dp.toPx() * (products.size - 1)
        }
    }

    Card(
        text = R.string.popular,
        spacer = 30.dp,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = if (hasEnoughSpace) {
                    Arrangement.SpaceBetween
                } else {
                    Arrangement.spacedBy(15.dp)
                }
            ) {
                products.forEach { product ->

                    val isInCart = cartItems.contains(product.id)
                    if (products.isEmpty()) {
                        EmptyContent(
                            icon = R.drawable.promotions,
                            visibleIcon = false,
                            emptyText = R.string.empty_popular
                        )
                    } else {
                        CardItem(
                            cardName = product.name,
                            cardImage = product.images.firstOrNull() ?: "",
                            money = product.price,
                            addedInCart = { addedInCart(product.id) },
                            liked = product.isFavorite,
                            cartIcon = isInCart,
                            addedInFavorite = { addedInFavorite(product.id, product.isFavorite) },
                            openCartScreen = openCartScreen,
                            openDetailScreen = {
                                openDetailScreen(product.id)
                            }
                        )
                    }
                }
            }
        },
        onClick = openPopularScreen
    )
}