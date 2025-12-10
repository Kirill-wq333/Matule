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

    when (val currentState = state) {
        is ArrivalsScreenContract.State.Loaded -> {
            ArrivalsContent(
                arrivals = currentState.promotions,
                onBack = onBack
            )
        }
        is ArrivalsScreenContract.State.Loading -> {
            LoadingScreen()
        }
        else -> {}
    }
}

@Composable
private fun ArrivalsContent(
    onBack: () -> Unit,
    arrivals: List<Promotion>
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
        Content(
            arrivals = arrivals
        )
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