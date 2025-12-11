package com.example.matule.ui.presentation.shared.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.R
import com.example.matule.ui.presentation.shared.screen.EmptyScreen
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Composable
fun SearchResult(
    searchResults: List<Product>,
    cartItems: Set<Long>,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit
) {

    if (searchResults.isEmpty()){
        EmptyScreen(
            modifier = Modifier.fillMaxSize(),
            icon = R.drawable.ic_search,
            emptyText = R.string.empty_search
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(searchResults, key = { it.id }){ product ->
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
}
@Composable
fun SearchScreenContent(
    searchQuery: String,
    searchResults: List<Product>,
    searchHistory: List<String>,
    cartItems: Set<Long>,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit,
    onSearchItemClick: (String) -> Unit,
    isSearchPerformed: Boolean
) {
    if (searchQuery.isEmpty() || !isSearchPerformed) {
        SearchHistorySection(
            searchHistory = searchHistory,
            onSearchItemClick = onSearchItemClick,
        )
    } else {
        SearchResult(
            searchResults = searchResults,
            cartItems = cartItems,
            addedInCart = addedInCart,
            addedInFavorite = addedInFavorite,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen
        )
    }
}

@Composable
fun SearchHistorySection(
    searchHistory: List<String>,
    onSearchItemClick: (String) -> Unit,
) {

    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(searchHistory) { historyItem ->
            SearchHistoryItem(
                query = historyItem,
                onClick = { onSearchItemClick(historyItem) }
            )
        }
    }
}

@Composable
fun SearchHistoryItem(
    query: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_clock),
            contentDescription = null,
            tint = Colors.subTextDark,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = query,
            style = MatuleTypography.bodyMedium,
            color = Colors.text
        )
    }

}