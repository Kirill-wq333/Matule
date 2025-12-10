package com.example.matule.ui.presentation.shared.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.matule.R
import com.example.matule.ui.presentation.feature.main.ui.Card
import com.example.matule.ui.presentation.shared.screen.EmptyContent
import com.example.matule.ui.presentation.theme.Colors
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ArrivalsCard(
    openArrivalsScreen: () -> Unit,
    promotions: List<Promotion>
) {
    var currentIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(promotions) {
        if (promotions.size > 1) {
            while (true) {
                delay(5000)
                currentIndex = (currentIndex + 1) % promotions.size
            }
        }
    }

    Card(
        text = R.string.new_arrivals,
        spacer = 20.dp,
        content = {
            if (promotions.isNotEmpty()) {
                val currentPromotion = promotions[currentIndex]

                AnimatedContent(
                    targetState = currentPromotion,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(1500),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) with slideOutHorizontally(
                            animationSpec = tween(1500),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        )
                    }
                ) { promotion ->
                    if (promotions.isEmpty()) {
                        EmptyContent(
                            icon = R.drawable.promotions,
                            emptyText = R.string.empty_arrivals
                        )
                    } else {
                        Arrivals(
                            arrivalsImage = promotion.image,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        },
        onClick = openArrivalsScreen
    )
}

@Composable
fun Arrivals(
    modifier: Modifier = Modifier,
    arrivalsImage: String
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .fillMaxWidth()
            .background(color = Colors.block, shape = RoundedCornerShape(16.dp))
            .height(95.dp)
    ){
        AsyncImage(
            model = arrivalsImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(1f)
        )
    }
}