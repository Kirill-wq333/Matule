package com.example.matule.ui.presentation.shared.main

import android.media.AudioRecord
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.catalog.model.Category
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Composable
fun CatalogCard(
    onCategorySelected: (Int) -> Unit,
    pagerState: PagerState,
    catalog: List<String>,
    categories: List<Product>,
    isSelected: Boolean
) {
    val lazyListState = rememberLazyListState()

    var wasSelected by remember { mutableStateOf(isSelected) }

    LaunchedEffect(pagerState.currentPage) {
        lazyListState.animateScrollToItem(pagerState.currentPage)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        Text(
            text = stringResource(R.string.category),
            color = Colors.text,
            style = MatuleTypography.titleMedium,
            modifier = Modifier.padding(start = 20.dp)
        )

        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(catalog) { index, category ->
                val isItemSelected = pagerState.currentPage == index
                Item(
                    text = category,
                    isSelected = if(wasSelected) isItemSelected else false,
                    onClick = {
                        wasSelected = true
                        onCategorySelected(index)
                    }
                )
            }
        }
    }
}

@Composable
fun CatalogProducts(
    products: List<Product>,
    cartItems: Set<Long>,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit
) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(products, key = { it.id }) { product ->
            val isInCart = cartItems.contains(product.id)

            CardItem(
                cardName = product.name,
                cardImage = product.images.firstOrNull() ?: "",
                money = product.price,
                addedInCart = { addedInCart(product.id) },
                liked = product.isFavorite,
                cartIcon = isInCart,
                addedInFavorite = { addedInFavorite(product.id, product.isFavorite) },
                openCartScreen = openCartScreen,
                openDetailScreen = { openDetailScreen(product.id) }
            )
        }
    }
}

@Composable
private fun Item(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val backColor by animateColorAsState(
        targetValue = if (isSelected) Colors.accent else Colors.block,
        animationSpec = tween(700)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Colors.block else Colors.text,
        animationSpec = tween(700)
    )

    Box(
        modifier = Modifier
            .width(108.dp)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .background(color = backColor, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MatuleTypography.bodySmall,
            modifier = Modifier
                .padding(top = 11.dp, bottom = 17.dp)
        )
    }
}