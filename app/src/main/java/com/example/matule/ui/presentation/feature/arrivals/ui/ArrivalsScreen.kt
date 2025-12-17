package com.example.matule.ui.presentation.feature.arrivals.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.matule.R
import com.example.matule.ui.presentation.feature.arrivals.viewmodel.ArrivalsScreenContract
import com.example.matule.ui.presentation.feature.arrivals.viewmodel.ArrivalsScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.main.Arrivals
import com.example.matule.ui.presentation.shared.pullToRefresh.PullRefreshLayout
import com.example.matule.ui.presentation.shared.screen.LoadingScreen
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun ArrivalsScreen(
    vm: ArrivalsScreenViewModel,
    onBack: () -> Unit,
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(ArrivalsScreenContract.Event.LoadedContent)
    }

    ArrivalsContent(
        onBack = onBack,
        state = state,
        onRefresh = {
            vm.handleEvent(ArrivalsScreenContract.Event.RefreshContent)
        }
    )

}

@Composable
private fun ArrivalsContent(
    onBack: () -> Unit,
    state: ArrivalsScreenContract.State,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomHeader(
            text = R.string.new_arrivals,
            onBack = onBack,
            visibleNameScreen = true,
        )
        when (state) {
            is ArrivalsScreenContract.State.Loaded -> {
                Content(
                    arrivals = state.promotions
                )
            }
            is ArrivalsScreenContract.State.Loading -> {
                LoadingScreen()
            }
            else -> {}
        }
    }
}

@Composable
private fun Content(
    arrivals: List<Promotion>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        arrivals.forEach { arrival ->
            Arrivals(arrivalsImage = arrival.image)
        }
    }
}