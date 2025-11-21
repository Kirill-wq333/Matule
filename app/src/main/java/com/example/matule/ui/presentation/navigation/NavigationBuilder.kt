package com.example.matule.ui.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.auth.ui.AuthScreen
import com.example.matule.ui.presentation.feature.main.ui.MainScreen
import com.example.matule.ui.presentation.feature.onboarding.ui.OnboardingScreen

@Composable
fun NavigationBuilder(
    navController: NavHostController,
    paddingValues: PaddingValues,
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
            MainScreen(
                navController = navController
            )
        }

        composable(
            AppRouts.AUTH,
            enterTransition = { fadeIn(tween(1500)) }
        ) {
            AuthScreen(
                navController = navController,
                openMainScreen = {
                }
            )
        }

        composable(AppRouts.ONBOARDING) {
            OnboardingScreen(
                navController = navController
            )
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