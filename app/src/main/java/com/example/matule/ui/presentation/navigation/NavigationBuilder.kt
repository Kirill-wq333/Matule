package com.example.matule.ui.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.auth.ui.AuthScreen
import com.example.matule.ui.presentation.feature.main.ui.MainScreen
import com.example.matule.ui.presentation.feature.main.ui.screenscomponents.ArrivalsScreen
import com.example.matule.ui.presentation.feature.main.ui.screenscomponents.PopularScreen
import com.example.matule.ui.presentation.feature.main.ui.screenscomponents.SideMenuScreen
import com.example.matule.ui.presentation.feature.main.viewmodel.MainViewModel
import com.example.matule.ui.presentation.feature.onboarding.ui.OnboardingScreen

@Composable
fun NavigationBuilder(
    navController: NavHostController,
    paddingValues: PaddingValues,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = Modifier
            .padding(paddingValues = paddingValues)
    ) {

        composable(
            AppRouts.MAIN,
            enterTransition = { fadeIn(tween(1500)) }
        ) {
            val vm = hiltViewModel<MainViewModel>()
            MainScreen(
                vm = vm,
                navController = navController
            )
        }

        composable(
            AppRouts.POPULAR,
            enterTransition = {
                fadeIn(tween(1500)) +
                        slideInHorizontally(
                            animationSpec = tween(1500),
                            initialOffsetX = { it }
                        )
            },
            exitTransition = {
                fadeOut(tween(1500)) +
                        slideOutHorizontally(
                            animationSpec = tween(1500),
                            targetOffsetX = { it }
                        )
            }
        ) {
            val vm = hiltViewModel<MainViewModel>()
            PopularScreen(
                vm = vm,
                onBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composable(
            AppRouts.ARRIVALS,
            enterTransition = {
                fadeIn(tween(1500)) +
                        slideInHorizontally(
                            animationSpec = tween(1500),
                            initialOffsetX = { it }
                        )
            },
            exitTransition = {
                fadeOut(tween(1500)) +
                        slideOutHorizontally(
                            animationSpec = tween(1500),
                            targetOffsetX = { it }
                        )
            }
        ) {
            val vm = hiltViewModel<MainViewModel>()
            ArrivalsScreen(
                vm = vm,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            AppRouts.SIDEMENU,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(800))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(800))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(800))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(800))
            }
        ) {
            SideMenuScreen(
                onBack = { navController.navigate(AppRouts.MAIN) },
                navController = navController
            )
        }

        composable(
            AppRouts.AUTH,
            enterTransition = { fadeIn(tween(1500)) }
        ) {
            AuthScreen(
                navController = navController,
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

        composable(AppRouts.PROFILE) {

        }

        composable(AppRouts.ORDERS) {

        }

        composable(AppRouts.NOTIFICATION) {

        }
    }
}