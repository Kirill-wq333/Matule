package com.example.matule.ui.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.auth.AuthScreen

@Composable
fun NavigationBuilder(
    navController: NavHostController,
    paddingValues: PaddingValues,
    visibleBottomBar: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppRouts.ONBOARDING,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = Modifier
            .padding(paddingValues = paddingValues)
    ) {

        composable(AppRouts.MAIN) {

        }

        composable(AppRouts.AUTH) {
            AuthScreen()
        }

        composable(AppRouts.ONBOARDING) {

        }

        composable(AppRouts.POPULAR) {

        }

        composable(AppRouts.CATALOG) {

        }

        composable(AppRouts.FAVOURITE) {

        }

        composable(AppRouts.DETAILS) {

        }

        composable(AppRouts.CART) {

        }

        composable(AppRouts.REGISTER_ACCOUNT) {

        }

        composable(AppRouts.FORGOT_PASSWORD) {

        }

        composable(AppRouts.VERIFICATION) {

        }

        composable(AppRouts.CHECKOUT) {

        }

        composable(AppRouts.PROFILE) {

        }

        composable(AppRouts.ORDERS) {

        }

        composable(AppRouts.NOTIFICATION) {

        }
    }
}