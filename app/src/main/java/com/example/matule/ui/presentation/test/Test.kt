package com.example.matule.ui.presentation.test

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Preview
@Composable
private fun PrevTest() {
    Test()
}

@Composable
fun Test() {
    val parentBoxWidth = 320.dp
    val boxSize = 50.dp

    var isDragging by remember { mutableStateOf(false) }
    val targetOffset = remember { mutableStateOf(0f) }
    val animatedOffset by animateFloatAsState(
        targetValue = targetOffset.value,
        animationSpec = tween(durationMillis = 300),
        label = "offsetAnimation"
    )


    // Храним текущее смещение
    var offsetX by remember { mutableStateOf(0f) }
    // Максимальное смещение (ширина контейнера - размер элемента)
    val maxOffset = parentBoxWidth - boxSize

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .width(parentBoxWidth)
                .align(Alignment.Center)
        ) {
            // Фоновая линия
            Box(
                Modifier
                    .width(parentBoxWidth)
                    .height(5.dp)
                    .background(Color.DarkGray)
                    .align(Alignment.CenterStart)
            )

            // Точки-якоря
            Box(
                Modifier
                    .size(10.dp)
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.CenterStart)
            )
            Box(
                Modifier
                    .size(10.dp)
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.Center)
            )
            Box(
                Modifier
                    .size(10.dp)
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.CenterEnd)
            )

            // Перетаскиваемый элемент с обычным draggable
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = if (isDragging) offsetX.roundToInt() else animatedOffset.roundToInt(),
                            y = 0
                        )
                    }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            isDragging = true
                            offsetX += delta
                        },
                        onDragStarted = {
                            isDragging = true
                        },
                        onDragStopped = {
                            isDragging = false
                            // Привязка к ближайшему якорю
                            val anchors = listOf(0f, maxOffset.value / 2, maxOffset.value)
                            val closestAnchor = anchors.minByOrNull { Math.abs(it - offsetX) } ?: 0f
                            targetOffset.value = closestAnchor
                            offsetX = closestAnchor
                        }
                    )
                    .size(boxSize)
                    .background(Color.LightGray)
            )
        }
    }
}
