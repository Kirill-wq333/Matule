package com.example.matule.ui.presentation.feature.cart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.example.domain.ui.presentation.feature.cart.model.CartState
import com.example.matule.R
import com.example.matule.ui.presentation.feature.cart.viewmodel.CartScreenContract
import com.example.matule.ui.presentation.feature.cart.viewmodel.CartScreenViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.cart.CartItem
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.screen.EmptyScreen
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private interface CartScreenCallback{
    fun onBack() {}
    fun openDetailScreen(id: Long) {}
    fun deleteProduct(id: Long) {}
    fun checkoutScreen() {}
    fun updateQuality(cartItemId: Long, newQuantity: Int) {}
}

@Composable
fun CartScreen(
    vm: CartScreenViewModel,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()

    var visibleCheckout by remember { mutableStateOf(false) }

    val callback = object : CartScreenCallback{
        override fun onBack() {
            if (visibleCheckout){
             visibleCheckout = false
            } else {
                navController.popBackStack()
            }
        }

        override fun checkoutScreen() {
            visibleCheckout = true
        }

        override fun deleteProduct(id: Long) {
            vm.handleEvent(CartScreenContract.Event.RemoveItem(id))
        }

        override fun openDetailScreen(id: Long) {
        }

        override fun updateQuality(cartItemId: Long, newQuantity: Int) {
            vm.handleEvent(CartScreenContract.Event.UpdateQuantity(cartItemId, newQuantity))
        }
    }

    CartContent(
        callback = callback,
        state = state
    )
}

@Composable
private fun CartContent(
    state: CartScreenContract.State,
    callback: CartScreenCallback,
) {
    Column(
        modifier = Modifier
            .background(color = Colors.background)
            .fillMaxSize()
    ) {
        CustomHeader(
            text = R.string.cart,
            visibleNameScreen = true,
            modifier = Modifier.padding(horizontal = 20.dp),
            onBack = callback::onBack
        )
        Spacer(modifier = Modifier.height(16.dp))
        when(state) {
            is CartScreenContract.State.Loaded -> {
                Cart(
                    state = state,
                    openCheckoutScreen = callback::checkoutScreen,
                    openDetailScreen = {
                        callback.openDetailScreen(it)
                    },
                    onDelete = {
                        callback.deleteProduct(it)
                    },
                    onPlusQuantity = { id, quality ->
                        callback.updateQuality(id, quality + 1)
                    },
                    onMinusQuantity = { id, quality ->
                        callback.updateQuality(id, quality - 1)
                    }
                )
            }
            is CartScreenContract.State.Loading -> {
                MainLoadingScreen()
            }
            is CartScreenContract.State.Empty -> {
                EmptyScreen(
                    icon = R.drawable.ic_cart,
                    emptyText = R.string.empty_cart
                )
            }
        }
    }
}

@Composable
fun Cart(
    state: CartScreenContract.State.Loaded,
    openCheckoutScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onPlusQuantity: (Long, Int) -> Unit,
    onMinusQuantity: (Long, Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Content(
            cartProduct = state.items,
            openDetailScreen = openDetailScreen,
            onDelete = onDelete,
            onPlusQuantity = onPlusQuantity,
            onMinusQuantity = onMinusQuantity
        )
        BottomBarCart(
            subtotal = state.subtotal,
            delivery = state.delivery,
            totalCost = state.totalPrice,
            openCheckoutScreen = openCheckoutScreen
        )
    }
}

@Composable
fun Content(
    cartProduct: List<CartItem>,
    openDetailScreen: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onPlusQuantity: (Long, Int) -> Unit,
    onMinusQuantity: (Long, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.cart_items, cartProduct.sumOf { it.quantity }),
            color = Colors.text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            cartProduct.forEach { cart ->

                val timeAgoInMinutes = calculateMinutesAgo(cart.createdAt)
                CartItem(
                    timeAgo = timeAgoInMinutes,
                    quantity = cart.quantity,
                    photoProduct = cart.product?.images?.first() ?: "",
                    nameProduct = cart.product?.name ?: "",
                    price = cart.product?.price ?: 0.0,
                    openDetailScreen = { openDetailScreen(cart.product?.id ?: 0) },
                    onDelete = { onDelete(cart.id) },
                    onMinusQuantity = { onMinusQuantity(cart.id, cart.quantity) },
                    onPlusQuantity = { onPlusQuantity(cart.id, cart.quantity) }
                )
            }
        }
    }
}

@Composable
fun BottomBarCart(
    subtotal: Double,
    delivery: Double,
    totalCost: Double,
    openCheckoutScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Colors.block)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 34.dp,
                bottom = 32.dp
            )
    ) {
        ComponentBottomBarCart(
            firstText = R.string.subtotal,
            secondText = subtotal
        )
        Spacer(modifier = Modifier.height(10.dp))
        ComponentBottomBarCart(
            firstText = R.string.delivery,
            secondText = delivery
        )
        Spacer(modifier = Modifier.height(18.dp))
        DashedLine(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))
        ComponentBottomBarCart(
            firstText = R.string.total_cost,
            secondText = totalCost
        )
        Spacer(modifier = Modifier.height(32.dp))
        CustomButton(
            text = R.string.btn_checkout,
            onClick = openCheckoutScreen,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ComponentBottomBarCart(
    firsTextColor: Color = Colors.subTextDark,
    secondTextColor: Color = Colors.text,
    firstText: Int,
    secondText: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(firstText),
            color = firsTextColor,
            style = MatuleTypography.bodyLarge
        )
        Text(
            text = stringResource(R.string.money, secondText),
            color = secondTextColor,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    color: Color = Colors.subTextDark,
    strokeWidth: Dp = 2.dp,
    dashLength: Float = 6f,
    gapLength: Float = 6f
) {
    Canvas(modifier = modifier) {
        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength, gapLength),
            phase = 0f
        )

        drawLine(
            color = color,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = strokeWidth.toPx(),
            pathEffect = pathEffect
        )
    }
}

fun calculateMinutesAgo(createdAt: String): Int {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val createdDate = dateFormat.parse(createdAt)
        val currentTime = System.currentTimeMillis()

        if (createdDate != null) {
            val diffMillis = currentTime - createdDate.time
            (diffMillis / (1000 * 60)).toInt()
        } else {
            0
        }
    } catch (e: Exception) {
        0
    }
}