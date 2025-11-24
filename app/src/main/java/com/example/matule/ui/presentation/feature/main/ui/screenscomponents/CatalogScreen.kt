package com.example.matule.ui.presentation.feature.main.ui.screenscomponents

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.main.model.Category
import com.example.domain.ui.presentation.feature.main.model.Product
import com.example.matule.R
import com.example.matule.ui.presentation.feature.main.ui.CatalogCard
import com.example.matule.ui.presentation.feature.main.viewmodel.MainScreenContract
import com.example.matule.ui.presentation.feature.main.viewmodel.MainViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.main.CardItem
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun CatalogScreen(
    categories: List<Category>,
    products: List<Product>,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit,
    addedInCart: (Long) -> Unit
) {


    CatalogContent(
        categories = categories,
        products = products,
        openCartScreen = openCartScreen,
        openDetailScreen = openDetailScreen,
        addedInCart = addedInCart
    )
}

@Composable
private fun CatalogContent(
    categories: List<Category>,
    products: List<Product>,
    openDetailScreen: (Long) -> Unit,
    addedInCart: (Long) -> Unit,
    openCartScreen: () -> Unit
) {

    Column(
        modifier = Modifier
            .background(color = Colors.background)
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomHeader(
            text = R.string.category
        )
        CatalogCard(
            openCatalogScreen = {},
            categories = categories
        )
        Content(
            products = products,
            openDetailScreen = openDetailScreen,
            addedInCart = addedInCart,
            openCartScreen = openCartScreen
        )
    }
}

@Composable
private fun Content(
    products: List<Product>,
    openCartScreen: () -> Unit,
    addedInCart: (Long) -> Unit,
    openDetailScreen: (Long) -> Unit
) {
    val lazyVerticalGridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = lazyVerticalGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(products) { product ->
            CardItem(
                cardName = product.name,
                cardImage = product.images.first(),
                money = product.price,
                addedInCart = {
                   addedInCart(product.id)
                },
                openCartScreen = openCartScreen,
                openDetailScreen = {
                    openDetailScreen(product.id)
                }
            )
        }
    }
}