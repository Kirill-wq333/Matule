package com.example.matule.ui.presentation.feature.orders.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.matule.R
import com.example.matule.ui.presentation.feature.orders.ui.functions.DayGroup
import com.example.matule.ui.presentation.feature.orders.ui.functions.formatDaysAgoSmart
import com.example.matule.ui.presentation.feature.orders.ui.functions.formatSmartTimeAgo
import com.example.matule.ui.presentation.feature.orders.ui.functions.formatYesterdayTimeSmart
import com.example.matule.ui.presentation.feature.orders.ui.functions.getDayGroupForOrder
import com.example.matule.ui.presentation.feature.orders.viewmodel.OrdersScreenContract
import com.example.matule.ui.presentation.feature.orders.viewmodel.OrdersScreenViewModel
import com.example.matule.ui.presentation.shared.cart.CartItem
import com.example.matule.ui.presentation.shared.cart.InformationSection
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.pullToRefresh.PullRefreshLayout
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun OrdersDetailScreen(
    vm: OrdersScreenViewModel,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()

    Content(
        onBack = {
            navController.popBackStack()
        },
        state = state,
        onRefresh = {
            vm.handleEvent(OrdersScreenContract.Event.Refresh)
        }
    )
}

@Composable
private fun Content(
    state: OrdersScreenContract.State,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.background)
            .padding(bottom = 25.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        when (state) {
            is OrdersScreenContract.State.OrderDetailsLoaded -> {
                CustomHeader(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = R.string.orders,
                    category = "${state.order.orderNumber}",
                    visibleNameScreen = true,
                    visibleText = false,
                    onBack = onBack
                )
                OrdersDetailContent(order = state.order)
            }
            is OrdersScreenContract.State.Loading -> {
                MainLoadingScreen()
            }
            else -> {}
        }
    }
}

@Composable
fun OrdersDetailContent(
    order: Order
) {
    val groupedOrder = getDayGroupForOrder(order)
    val timeText = when (groupedOrder) {
        DayGroup.TODAY -> formatSmartTimeAgo(order.createdAt)
        DayGroup.YESTERDAY -> formatYesterdayTimeSmart(order.createdAt)
        DayGroup.RECENTLY -> formatDaysAgoSmart(order.createdAt)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        Text(
            text = timeText,
            color = Colors.hint,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            lineHeight = 20.sp,
            modifier = Modifier.padding(end = 22.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                order.items.forEach { item ->
                    CartItem(
                        quantity = item.quantity,
                        nameProduct = item.name,
                        openDetailScreen = {},
                        photoProduct = item.image,
                        price = item.price,
                        orders = false,
                        visibleTA = false
                    )
                }
            }

            InformationSection(
                address = order.address.street,
                city = order.address.city,
                country = order.address.country,
                postalCode = order.address.postalCode,
                phone = order.contactInfo.phone,
                visibleEndIcon = false,
                email = order.contactInfo.email
            )
        }
    }
}