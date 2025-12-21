package com.example.matule

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
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

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    @Inject
    lateinit var appStartManager: AppStartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

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

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {}

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> { requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }

                else -> { requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
            }
        }
    }

}