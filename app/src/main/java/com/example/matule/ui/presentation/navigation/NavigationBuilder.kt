package com.example.matule.ui.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.matule.ui.presentation.feature.arrivals.ui.ArrivalsScreen
import com.example.matule.ui.presentation.feature.arrivals.viewmodel.ArrivalsScreenViewModel
import com.example.matule.ui.presentation.feature.register.ui.RegisterScreen
import com.example.matule.ui.presentation.feature.auth.viewmodel.AuthScreenViewModel
import com.example.matule.ui.presentation.feature.favorite.ui.FavoriteScreen
import com.example.matule.ui.presentation.feature.favorite.viewmodel.FavoriteScreenViewModel
import com.example.matule.ui.presentation.feature.popular.ui.PopularScreen
import com.example.matule.ui.presentation.feature.sidemenu.ui.SideMenuScreen
import com.example.matule.ui.presentation.feature.main.viewmodel.MainViewModel
import com.example.matule.ui.presentation.feature.notification.ui.NotificationScreen
import com.example.matule.ui.presentation.feature.notification.viewmodel.NotificationScreenViewModel
import com.example.matule.ui.presentation.feature.onboarding.ui.OnboardingScreen
import com.example.matule.ui.presentation.feature.onboarding.viewmodel.OnboardingViewModel
import com.example.matule.ui.presentation.feature.popular.viewmodel.PopularScreenViewModel
import com.example.matule.ui.presentation.feature.profile.ui.ProfileScreen
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenViewModel
import com.example.matule.ui.presentation.feature.register.viewmodel.RegisterViewModel
import com.example.matule.ui.presentation.feature.sidemenu.viewmodel.SideMenuScreenViewModel

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
            val vmMain = hiltViewModel<MainViewModel>()
            MainScreen(
                vm = vmMain,
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
            val vmPopular = hiltViewModel<PopularScreenViewModel>()
            PopularScreen(
                vm = vmPopular,
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
            val vmArrivals = hiltViewModel<ArrivalsScreenViewModel>()
            ArrivalsScreen(
                vm = vmArrivals,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            AppRouts.SIDE_MENU,
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
            val vmSideMenu = hiltViewModel<SideMenuScreenViewModel>()

            SideMenuScreen(
                vm = vmSideMenu,
                onBack = { navController.navigate(AppRouts.MAIN) },
                navController = navController
            )
        }

        composable(
            AppRouts.AUTH,
            enterTransition = { fadeIn(tween(1500)) }
        ) {
            val vmAuth = hiltViewModel<AuthScreenViewModel>()

            AuthScreen(
                vm = vmAuth,
                navController = navController,
            )
        }

        composable(AppRouts.ONBOARDING) {
            val vmOnboarding = hiltViewModel<OnboardingViewModel>()
            OnboardingScreen(
                vm = vmOnboarding,
                navController = navController
            )
        }

        composable(AppRouts.FORGOT_PASSWORD) {

        }

        composable(AppRouts.REGISTER) {
            val vmRegister = hiltViewModel<RegisterViewModel>()
            RegisterScreen(
                vm = vmRegister,
                navController = navController
            )
        }

        composable(AppRouts.FAVOURITE) {
            val vmFavorite = hiltViewModel<FavoriteScreenViewModel>()
            FavoriteScreen(
                vm = vmFavorite,
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(AppRouts.DETAILS) {

        }

        composable(AppRouts.CART) {

        }

        composable(AppRouts.PROFILE) {
            val vmProfile = hiltViewModel<ProfileScreenViewModel>()
            ProfileScreen(
                vm = vmProfile
            )
        }

        composable(AppRouts.ORDERS) {

        }

        composable(AppRouts.NOTIFICATION) {
            val vmNotification = hiltViewModel<NotificationScreenViewModel>()

            NotificationScreen(
                vm = vmNotification,
                openSideMenu = { navController.navigate(AppRouts.SIDE_MENU) }
            )
        }
    }
}