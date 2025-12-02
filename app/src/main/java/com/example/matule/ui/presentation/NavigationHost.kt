package com.example.matule.ui.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                startDestination = startDestination,
                shouldShowBottomBar = shouldShowBottomBar
            )
        }

    )
}

@Composable
private fun HostScaffold(
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        content = content
    )
}