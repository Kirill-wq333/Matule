package com.example.matule.ui.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.navigation.NavigationBuilder
import com.example.matule.ui.presentation.navigation.bottomBar.BottomBarNavigation

@Composable
fun NavigationHost(
    navController: NavHostController?
) {
    var visibleBottomBar by remember { mutableStateOf(false) }

    if (navController == null) return
    HostScaffold(
        content = { paddingValues ->
            NavigationBuilder(
                navController = navController,
                paddingValues = paddingValues,
                visibleBottomBar = { visibleBottomBar = it }
            )
        },
        bottomBar = {
            if (visibleBottomBar)
            BottomBarNavigation(
                navController = navController,
                openCartScreen = { navController.navigate(AppRouts.CART) }
            )
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