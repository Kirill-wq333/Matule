package com.example.matule.ui.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.navigation.NavigationBuilder

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
    Scaffold(
        content = { paddingValues ->
            NavigationBuilder(
                navController = navController,
                paddingValues = paddingValues,
                startDestination = startDestination,
                shouldShowBottomBar = shouldShowBottomBar
            )
        }
    )
}