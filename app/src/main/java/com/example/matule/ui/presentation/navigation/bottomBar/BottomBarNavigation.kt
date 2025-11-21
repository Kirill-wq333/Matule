package com.example.matule.ui.presentation.navigation.bottomBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.theme.Colors

data class BottomBarNavigationItem(
    val route: String,
    val icon: Int
)

@Preview
@Composable
private fun Prev() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxWidth().background(Colors.background)) {
        BottomBarNavigation(navController = navController) {}
    }
}

@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    openCartScreen: () -> Unit
) {

    val firstButtons = listOf(
        BottomBarNavigationItem(
            route = AppRouts.MAIN,
            icon = R.drawable.ic_home
        ),
        BottomBarNavigationItem(
            route = AppRouts.FAVOURITE,
            icon = R.drawable.ic_favorite
        )
    )

    val secondButtons = listOf(
        BottomBarNavigationItem(
            route = AppRouts.NOTIFICATION,
            icon = R.drawable.ic_notification
        ),
        BottomBarNavigationItem(
            route = AppRouts.PROFILE,
            icon = R.drawable.ic_profile
        )
    )

    BottomContent(
        navController = navController,
        firstButtons = firstButtons,
        secondButtons = secondButtons,
        openCartScreen = openCartScreen
    )
}

@Composable
fun BottomContent(
    navController: NavHostController,
    firstButtons: List<BottomBarNavigationItem>,
    secondButtons: List<BottomBarNavigationItem>,
    openCartScreen: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Colors.background)
                .heightIn(max = 105.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.bottom_bar),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        FloatingActionButton(
            onClick = openCartScreen,
            shape = CircleShape,
            containerColor = Colors.accent,
            contentColor = Colors.block,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_cart),
                contentDescription = null,
            )
        }
        Actions(
            navController = navController,
            firstButtons = firstButtons,
            secondButtons = secondButtons
        )
    }
}

@Composable
fun BoxScope.Actions(
    navController: NavHostController,
    firstButtons: List<BottomBarNavigationItem>,
    secondButtons: List<BottomBarNavigationItem>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 31.dp,
                end = 31.dp,
                bottom = 30.dp
            )
            .align(Alignment.BottomCenter),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        CustomButtonItemInBottom(
            navController = navController,
            buttons = firstButtons
        )

        CustomButtonItemInBottom(
            navController = navController,
            buttons = secondButtons
        )
    }
}

@Composable
private fun CustomButtonItemInBottom(
    navController: NavHostController,
    buttons: List<BottomBarNavigationItem>
) {
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route


    Row(
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        buttons.forEach { item ->
            val isSelected = currentRoute == item.route
            val tint = if (isSelected) Colors.accent else Colors.subTextDark


            val colorState by animateColorAsState(
                targetValue = tint,
                animationSpec = tween(500)
            )

            Icon(
                imageVector = ImageVector.vectorResource(item.icon),
                contentDescription = null,
                tint = colorState,
                modifier = Modifier.clickable{ navController.navigate(item.route) }
            )
        }
    }
}