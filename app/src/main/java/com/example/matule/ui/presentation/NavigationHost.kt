package com.example.matule.ui.presentation

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
            if (shouldShowBottomBar) {
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