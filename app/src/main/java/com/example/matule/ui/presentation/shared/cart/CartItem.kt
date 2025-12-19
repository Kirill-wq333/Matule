@file:OptIn(ExperimentalFoundationApi::class)

package com.example.matule.ui.presentation.shared.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors
import kotlinx.coroutines.launch

@Preview
@Composable
private fun PreviewCartItem() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CartItem(
            quantity = 1,
            timeAgo = "5",
            nameProduct = "Nike Air Max 270 Essential",
            delivery = 0.0,
            price = 1436.0,
            openDetailScreen = {},
            numberOrders = 0,
            orders = false,
        )
        CartItem(
            quantity = 1,
            timeAgo = "5",
            visibleCartComponents = false,
            nameProduct = "Nike Air Max 270 Essential",
            delivery = 364.0,
            price = 125.0,
            openDetailScreen = {},
            numberOrders = 35252,
            orders = true,
        )
    }
}


enum class SlideState { LEFT, CENTER, RIGHT }

@Composable
fun CartItem(
    quantity: Int = 1,
    photoProduct: String? = "",
    timeAgo: String = "",
    nameProduct: String,
    price: Double,
    numberOrders: Long = 0,
    openDetailScreen: () -> Unit,
    onUpdateOrder: () -> Unit = {},
    delivery: Double = 0.0,
    onDelete: () -> Unit = {},
    visibleQuantity: Boolean = true,
    orders: Boolean = false,
    visibleTA: Boolean = true,
    onPlusQuantity: () -> Unit = {},
    onMinusQuantity: () -> Unit = {},
    visibleCartComponents: Boolean = true,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    var visibleOrders by remember { mutableStateOf(orders) }
    var isShiftedLeft by remember { mutableStateOf(false) }
    var isShiftedRight by remember { mutableStateOf(false) }
    var visibleTimeAgo by remember { mutableStateOf(visibleTA) }
    var visibleQuantity by remember { mutableStateOf(visibleQuantity) }

    val widthFraction = if (isShiftedLeft || isShiftedRight) 0.82f else 1f
    var dragOffset by remember { mutableStateOf(0f) }
    val alignment = when {
        isShiftedLeft -> Alignment.CenterEnd
        isShiftedRight -> Alignment.CenterStart
        else -> Alignment.Center
    }

    val actionPanelWidthPx = with(density) { 80.dp.toPx() }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val dragState = remember {
        AnchoredDraggableState(
            initialValue = SlideState.CENTER,
            anchors = DraggableAnchors {
                SlideState.LEFT at -actionPanelWidthPx
                SlideState.CENTER at 0f
                SlideState.RIGHT at actionPanelWidthPx
            },
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { 125f },
            snapAnimationSpec = tween(700),
            decayAnimationSpec = decayAnimationSpec
        )
    }

    LaunchedEffect(dragState.currentValue) {
        when (dragState.currentValue) {
            SlideState.LEFT -> {
                isShiftedLeft = false
                isShiftedRight = true
                visibleOrders = false
                visibleTimeAgo = false
            }

            SlideState.RIGHT -> {
                isShiftedLeft = true
                isShiftedRight = false
                visibleOrders = false
                visibleTimeAgo = false
            }

            SlideState.CENTER -> {
                isShiftedLeft = false
                isShiftedRight = false
                visibleOrders = orders
                visibleTimeAgo = visibleTA
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {

        AnimatedVisibility(
            visible = isShiftedLeft,
            modifier = Modifier
                .align(Alignment.CenterStart),
            enter = fadeIn(tween(500))
                    + slideInHorizontally(tween(500)) { -it },
            exit = fadeOut(tween(500))
                    + slideOutHorizontally(tween(500)) { -it }
        ) {
            if (visibleCartComponents) {
                QuantityItem(
                    quantity = quantity,
                    onPlusQuantity = onPlusQuantity,
                    onMinusQuantity = onMinusQuantity
                )
            } else {
                BoxIcon(
                    icon = R.drawable.ic_reload,
                    backColor = Colors.accent,
                    verticalPadding = 36.dp,
                    horizontalPadding = 13.dp,
                    width = 32.dp,
                    height = 32.dp,
                    onClick = {
                        onUpdateOrder()
                        coroutineScope.launch {
                            dragState.animateTo(SlideState.CENTER)
                        }
                    },
                )
            }
        }


        AnimatedVisibility(
            visible = isShiftedRight,
            modifier = Modifier
                .align(Alignment.CenterEnd),
            enter = fadeIn(tween(500))
                    + slideInHorizontally(tween(500)) { it },
            exit = fadeOut(tween(500))
                    + slideOutHorizontally(tween(500)) { it }
        ) {
            if (visibleCartComponents) {
                BoxIcon(
                    icon = R.drawable.ic_trash,
                    backColor = Colors.red,
                    onClick = {
                        onDelete()
                        coroutineScope.launch {
                            dragState.animateTo(SlideState.CENTER)
                        }
                    }
                )
            } else {
                BoxIcon(
                    icon = R.drawable.ic_cancel,
                    backColor = Colors.red,
                    verticalPadding = 36.dp,
                    horizontalPadding = 13.dp,
                    width = 32.dp,
                    height = 32.dp,
                    onClick = {
                        onDelete()
                        coroutineScope.launch {
                            dragState.animateTo(SlideState.CENTER)
                        }
                    },
                )
            }
        }
        CartContent(
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .align(alignment)
                .offset(x = with(density) { dragOffset.toDp() })
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Horizontal
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (dragState.currentValue != SlideState.CENTER) {
                                coroutineScope.launch {
                                    dragState.animateTo(SlideState.CENTER)
                                }
                            } else {
                                openDetailScreen()
                            }
                        }
                    )
                },
            photoProduct = photoProduct,
            timeAgo = timeAgo,
            nameProduct = nameProduct,
            price = price,
            delivery = delivery,
            visibleOrders = visibleOrders,
            numberOrders = numberOrders,
            openDetailScreen = {
                if (dragState.currentValue != SlideState.CENTER) {
                    coroutineScope.launch {
                        dragState.animateTo(SlideState.CENTER)
                    }
                } else {
                    openDetailScreen()
                }
            },
            visibleTimeAgo = visibleTimeAgo,
            quantity = quantity,
            visibleCartComponents = visibleCartComponents,
            visibleQuantity = visibleQuantity
        )
    }
}

@Composable
fun BoxIcon(
    modifier: Modifier = Modifier,
    icon: Int,
    backColor: Color,
    verticalPadding: Dp = 42.dp,
    horizontalPadding: Dp = 20.dp,
    onClick: () -> Unit,
    width: Dp = 18.dp,
    height: Dp = 20.dp,
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(color = backColor, shape = RoundedCornerShape(8.dp)),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = Colors.block,
            modifier = Modifier
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .size(width, height)
        )
    }
}

@Composable
private fun CartContent(
    modifier: Modifier = Modifier,
    photoProduct: String?,
    timeAgo: String,
    nameProduct: String,
    price: Double,
    numberOrders: Long,
    delivery: Double,
    visibleQuantity: Boolean,
    visibleCartComponents: Boolean,
    openDetailScreen: () -> Unit,
    visibleOrders: Boolean,
    quantity: Int,
    visibleTimeAgo: Boolean
) {

    Box(
        modifier = modifier
            .background(color = Colors.block, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 9.dp, top = 9.dp, end = 33.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(87.dp, 85.dp)
                    .background(color = Colors.background, shape = RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = photoProduct,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.image_placeholder),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 18.dp)
                )
            }
            Column(
                modifier = Modifier
                    .clickable(onClick = openDetailScreen),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                if (visibleOrders && !visibleCartComponents) {
                    Text(
                        text = stringResource(R.string.number_orders, numberOrders),
                        color = Colors.accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                    )
                }
                Text(
                    text = nameProduct,
                    color = Colors.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(if (quantity > 1) 24.dp else 39.dp)
                ) {
                    Text(
                        text = stringResource(R.string.money, price),
                        color = Colors.text,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                    )
                    if (quantity > 0 && visibleQuantity){
                        Text(
                            text = "$quantity ШТ",
                            color = Colors.hint,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                        )
                    }
                    if (visibleOrders && !visibleCartComponents) {
                        Text(
                            text = "$delivery",
                            color = Colors.hint,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                        )
                    }
                }
            }
        }
        if (visibleTimeAgo && !visibleCartComponents) {
            Text(
                text = timeAgo,
                color = Colors.hint,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                lineHeight = 20.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
            )
        }
    }
}

@Composable
fun QuantityItem(
    modifier: Modifier = Modifier,
    quantity: Int = 1,
    onPlusQuantity: () -> Unit,
    onMinusQuantity: () -> Unit
) {

    Column(
        modifier = modifier
            .background(color = Colors.accent, shape = RoundedCornerShape(8.dp))
            .padding(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 13.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_add),
            contentDescription = null,
            tint = Colors.block,
            modifier = Modifier
                .clickable(onClick = onPlusQuantity)
                .size(14.dp)
        )
        Spacer(modifier = Modifier.height(23.dp))
        Text(
            text = "$quantity",
            color = Colors.block,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(
            modifier = Modifier
                .clickable(onClick = onMinusQuantity)
                .width(14.dp),
            thickness = 1.dp,
            color = Colors.block
        )
    }
}

