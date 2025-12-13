package com.example.matule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.matule.ui.manage.AppStartManager
import com.example.matule.ui.presentation.NavigationHost
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.MatuleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appStartManager: AppStartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val startDestination = remember { mutableStateOf(AppRouts.ONBOARDING) }
            val isLoading = remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                val destination = appStartManager.getStartDestination()
                startDestination.value = destination
                isLoading.value = false
            }

            MatuleTheme {
                if (isLoading.value) {
                    MainLoadingScreen()
                } else {
                    val navController = rememberNavController()
                    NavigationHost(
                        navController = navController,
                        startDestination = startDestination.value
                    )
                }
            }
        }
    }
}