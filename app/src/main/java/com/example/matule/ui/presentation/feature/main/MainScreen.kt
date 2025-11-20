package com.example.matule.ui.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.main.CardItem
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

private interface MainScreenCallback{
    fun openCatalogScreen() {}
    fun openPopularScreen() {}
    fun openCartScreen() {}
    fun openArrivalsScreen() {}
    fun openDetailScreen() {}
}

@Composable
fun MainScreen(
    navController: NavHostController
) {

    val callback = object : MainScreenCallback{
        override fun openCatalogScreen() {
            navController.navigate(AppRouts.CATALOG)
        }

        override fun openPopularScreen() {
            navController.navigate(AppRouts.POPULAR)
        }

        override fun openCartScreen() {
            navController.navigate(AppRouts.CART)
        }

        override fun openArrivalsScreen() {
            navController.navigate(AppRouts.ARRIVALS)
        }

        override fun openDetailScreen() {
            navController.navigate(AppRouts.DETAILS)
        }
    }

    Main(
        callback = callback
    )

}

@Preview
@Composable
private fun Preview() {
    val callback = object : MainScreenCallback{
        override fun openCatalogScreen() {

        }

        override fun openPopularScreen() {

        }

        override fun openCartScreen() {

        }

        override fun openArrivalsScreen() {

        }
    }

    Main(callback = callback)
}

@Composable
private fun Main(
    callback: MainScreenCallback
) {


    Content(
        openPopularScreen = callback::openPopularScreen,
        openCatalogScreen = callback::openCatalogScreen,
        openArrivalsScreen = callback::openArrivalsScreen,
        openCartScreen = callback::openCartScreen,
        cardName = "",
        cardImage = "https://cdn.imgbin.com/21/14/17/imgbin-skate-shoe-sneakers-nike-converse-nike-wgVVd0RPVGxgPcrtg39U9Kajd.jpg",
        money = 214,
        addedInCart = { },
        openDetailScreen = { },
        openSideMenu = { },
    )
}

@Composable
private fun Content(
    cardName: String,
    cardImage: String,
    money: Int,
    addedInCart: (Int) -> Unit = {},
    openCartScreen: () -> Unit = {},
    openDetailScreen: () -> Unit = {},
    openPopularScreen: () -> Unit,
    openSideMenu: () -> Unit,
    openCatalogScreen: () -> Unit,
    openArrivalsScreen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.background)
            .padding(horizontal = 20.dp)
    ) {
        CustomHeaderMain(
            text = R.string.main,
            cardItem = 0,
            openSideMenu = openSideMenu,
            openCartScreen = openCartScreen
        )

        Spacer(modifier = Modifier.height(22.dp))
        CatalogCard()
        Spacer(modifier = Modifier.height(24.dp))
        PopularCard(
            openPopularScreen = openPopularScreen,
            cardName = cardName,
            cardImage = cardImage,
            money = money,
            addedInCart = addedInCart,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen,
        )
        Spacer(modifier = Modifier.height(29.dp))
        ArrivalsCard(
            openArrivalsScreen = openArrivalsScreen
        )
    }

}

@Composable
private fun PopularCard(
    openPopularScreen: () -> Unit,
    cardName: String,
    cardImage: String,
    money: Int,
    addedInCart: (Int) -> Unit = {},
    openCartScreen: () -> Unit = {},
    openDetailScreen: () -> Unit = {}
) {
    Card(
        text = R.string.popular,
        spacer = 30.dp,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(2) {
                    CardItem(
                        cardName = cardName,
                        cardImage = cardImage,
                        money = money,
                        addedInCart = addedInCart,
                        openCartScreen = openCartScreen,
                        openDetailScreen = openDetailScreen
                    )
                }
            }
        },
        onClick = openPopularScreen
    )
}

@Composable
private fun CatalogCard() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        Text(
            text = stringResource(R.string.category),
            color = Colors.text,
            style = MatuleTypography.titleMedium
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) {
                Item(
                    text = "fghf"
                )
            }
        }
    }
}

@Composable
private fun ArrivalsCard(
    openArrivalsScreen: () -> Unit
) {
    Card(
        text = R.string.new_arrivals,
        spacer = 20.dp,
        content = {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
            )
        },
        onClick = openArrivalsScreen
    )
}

@Composable
private fun Card(
    text: Int,
    content: @Composable () -> Unit,
    spacer: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(text),
                color = Colors.text,
                style = MatuleTypography.titleMedium
            )
            Text(
                text = stringResource(R.string.see_all),
                color = Colors.accent,
                style = MatuleTypography.bodySmall,
                modifier = Modifier.clickable(onClick = onClick)
            )
        }
        content()
    }
}

@Composable
private fun Item(
    text: String
) {
    Box(
        modifier = Modifier
            .width(108.dp)
            .background(color = Colors.block, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Colors.text,
            style = MatuleTypography.bodySmall,
            modifier = Modifier.padding(top = 11.dp, bottom = 17.dp)
        )
    }
}