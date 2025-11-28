package com.example.matule.ui.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.navigation.NavigationBuilder
import com.example.matule.ui.presentation.navigation.bottomBar.BottomBarNavigation

@Composable
fun NavigationHost(
    navController: NavHostController?,
    startDestination: String
) {

    val screensWithBottomBar = listOf(
        AppRouts.MAIN,
        AppRouts.FAVOURITE,
        AppRouts.NOTIFICATION,
        AppRouts.PROFILE
    )

    val currentDestination = navController
        ?.currentBackStackEntryAsState()
        ?.value
        ?.destination
        ?.route

    val shouldShowBottomBar = currentDestination in screensWithBottomBar

    if (navController == null) return
    HostScaffold(
        content = { paddingValues ->
            NavigationBuilder(
                navController = navController,
                paddingValues = paddingValues,
                startDestination = startDestination
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter =  slideInVertically(tween(700)) { it } + fadeIn(tween(700)),
                exit = slideOutVertically(tween(700)){ it } + fadeOut(tween(700))
            ) {
                BottomBarNavigation(
                    navController = navController,
                    openCartScreen = { navController.navigate(AppRouts.CART) }
                )
            }
        }
    )
}

@Composable
private fun HostScaffold(
    content: @Composable (PaddingValues) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    Scaffold(
        content = content,
        bottomBar = bottomBar
    )

}