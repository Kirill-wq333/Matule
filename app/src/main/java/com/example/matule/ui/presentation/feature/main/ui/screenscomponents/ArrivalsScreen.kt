package com.example.matule.ui.presentation.feature.main.ui.screenscomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.main.CardItem
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun ArrivalsScreen() {

}

@Composable
private fun ArrivalsContent(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomHeader(
            text = R.string.popular,
            endIcon = R.drawable.ic_favorite,
            onBack = onBack,
            openScreen = {}
        )
        Content()
    }

}

@Composable
fun Content() {
    val lazyVerticalGridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = lazyVerticalGridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            CardItem(
                cardName = "",
                cardImage = "",
                money = 1251,
                addedInCart = {},
                openCartScreen = {},
                openDetailScreen = {}
            )
        }
    }
}