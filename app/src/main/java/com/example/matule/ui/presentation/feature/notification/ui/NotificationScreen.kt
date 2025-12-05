package com.example.matule.ui.presentation.feature.notification.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.notification.model.Notifications
import com.example.matule.R
import com.example.matule.ui.presentation.feature.notification.viewmodel.NotificationScreenContract
import com.example.matule.ui.presentation.feature.notification.viewmodel.NotificationScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.notification.NotificationItem
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Composable
fun NotificationScreen(
    vm: NotificationScreenViewModel,
    openSideMenu: () -> Unit
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(NotificationScreenContract.Event.LoadNotifications)
    }

    Content(
        state = state,
        openSideMenu = openSideMenu
    )
}

@Composable
private fun Content(
    openSideMenu: () -> Unit,
    state: NotificationScreenContract.State
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.block)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomHeaderMain(
            text = R.string.notifications,
            backColor = Color.Unspecified,
            style = MatuleTypography.bodyLarge,
            visibleEndIcon = false,
            visibleCosmeticIcon = false,
            openSideMenu = openSideMenu
        )
        when(state) {
            is NotificationScreenContract.State.Loaded -> {
                NotificationContent(
                    notifications = state.notifications
                )
            }
            is NotificationScreenContract.State.Loading -> {
                MainLoadingScreen()
            }
            is NotificationScreenContract.State.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_notification),
                            contentDescription = null,
                            tint = Colors.text,
                            modifier = Modifier.size(96.dp)
                        )
                        Text(
                            text = stringResource(R.string.empty_notification),
                            color = Colors.text,
                            style = MatuleTypography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun NotificationContent(
    notifications: List<Notifications>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = notifications,
            key = { it.id }
        ) { notification ->
            NotificationItem(
                heading = notification.title,
                description = notification.message,
                date = notification.createdAt
            )
        }
    }
}