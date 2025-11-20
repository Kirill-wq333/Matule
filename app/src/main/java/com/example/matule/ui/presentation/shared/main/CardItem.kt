package com.example.matule.ui.presentation.shared.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Preview
@Composable
private fun PreviewCard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CardItem(
                cardName = "Nike Air Max",
                cardImage = "https://cdn.imgbin.com/21/14/17/imgbin-skate-shoe-sneakers-nike-converse-nike-wgVVd0RPVGxgPcrtg39U9Kajd.jpg",
                money = 773
            )
            CardItem(
                cardName = "Nike Air Max",
                cardImage = "https://cdn.imgbin.com/21/14/17/imgbin-skate-shoe-sneakers-nike-converse-nike-wgVVd0RPVGxgPcrtg39U9Kajd.jpg",
                money = 773
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CardItem(
                cardName = "Nike Air Max",
                cardImage = "https://cdn.imgbin.com/21/14/17/imgbin-skate-shoe-sneakers-nike-converse-nike-wgVVd0RPVGxgPcrtg39U9Kajd.jpg",
                money = 773
            )
            CardItem(
                cardName = "Nike Air Max",
                cardImage = "https://cdn.imgbin.com/21/14/17/imgbin-skate-shoe-sneakers-nike-converse-nike-wgVVd0RPVGxgPcrtg39U9Kajd.jpg",
                money = 773
            )
        }
    }
}


@Composable
fun CardItem(
    cardName: String,
    cardImage: String,
    money: Int,
    addedInCart: (Int) -> Unit = {},
    openCartScreen: () -> Unit = {},
    openDetailScreen: () -> Unit = {}
) {

    var liked by remember { mutableStateOf(false) }
    var cartIcon by remember { mutableStateOf(false) }

    val icon = if (liked) R.drawable.ic_favorite_fill else R.drawable.ic_favorite
    val colorIcon = if (liked) Colors.red else Colors.text

    Box(
        modifier = Modifier
            .width(160.dp)
            .background(
                color = Colors.block,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        CustomIconButton(
            icon = icon,
            padding = 6.dp,
            size = 16.dp,
            backColor = Colors.background,
            modifier = Modifier.padding(9.dp),
            tint = colorIcon,
            onClick = { liked = !liked }
        )

        Content(
            cardName = cardName,
            cardImage = cardImage,
            money = money,
            cartIcon = cartIcon,
            addedInCart = addedInCart,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen
        )
    }
}

@Composable
fun Content(
    cardName: String,
    cardImage: String,
    money: Int,
    cartIcon: Boolean,
    addedInCart: (Int) -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CardContent(
            cardName = cardName,
            cardImage = cardImage,
            openDetailScreen = openDetailScreen
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.money, money),
                color = Colors.text,
                style = MatuleTypography.bodyMedium,
                modifier = Modifier.padding(start = 9.dp)
            )

            if (cartIcon) {
                CardIconButton(
                    icon = R.drawable.ic_cart,
                    onClick = openCartScreen,
                    modifier = Modifier
                        .padding(11.dp)
                        .size(12.dp)
                )
            } else {
                CardIconButton(
                    icon = R.drawable.ic_add,
                    onClick = {
                        addedInCart
                        cartIcon
                    },
                    modifier = Modifier
                        .padding(start = 7.dp, end = 6.dp, top = 8.86.dp, bottom = 9.82.dp)
                        .size(width = 21.dp, height = 15.32.dp)
                )
            }
        }
    }
}

@Composable
private fun CardIconButton(
    modifier: Modifier = Modifier,
    icon: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                color = Colors.accent,
                shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
            )
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = Colors.block,
            modifier = modifier
        )
    }
}

@Composable
private fun CardContent(
    cardImage: String,
    cardName: String,
    openDetailScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                top = 18.dp,
                start = 9.dp,
                end = 9.dp
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = cardImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(height = 70.dp, width = 118.dp),
            )
        }
        Column(
            modifier = Modifier.clickable(onClick = openDetailScreen),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.best_seller),
                color = Colors.accent,
                style = MatuleTypography.bodySmall
            )
            Text(
                text = cardName,
                color = Colors.hint,
                style = MatuleTypography.bodyLarge
            )
        }
    }
}