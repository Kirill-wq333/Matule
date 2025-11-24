package com.example.matule.ui.presentation.feature.main.ui.screenscomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.ui.presentation.feature.main.model.Promotion
import com.example.matule.R
import com.example.matule.ui.presentation.feature.main.ui.Arrivals
import com.example.matule.ui.presentation.feature.main.viewmodel.MainScreenContract
import com.example.matule.ui.presentation.feature.main.viewmodel.MainViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun ArrivalsScreen(
    vm: MainViewModel,
    onBack: () -> Unit,
) {

    val state by vm.state.collectAsState()

    when (val currentState = state) {
        is MainScreenContract.State.Loaded -> {
            ArrivalsContent(
                arrivals = currentState.promotions,
                onBack = onBack
            )
        }
        is MainScreenContract.State.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.background),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Загрузка товаров...")
                }
            }
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
            text = R.string.popular,
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