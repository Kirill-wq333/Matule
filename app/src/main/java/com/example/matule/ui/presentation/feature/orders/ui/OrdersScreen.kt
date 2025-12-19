package com.example.matule.ui.presentation.feature.orders.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.domain.ui.presentation.feature.orders.model.Order
import com.example.domain.ui.presentation.feature.orders.model.OrderStatus
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.orders.ui.functions.DayGroup
import com.example.matule.ui.presentation.feature.orders.ui.functions.formatDaysAgoSmart
import com.example.matule.ui.presentation.feature.orders.ui.functions.formatSmartTimeAgo
import com.example.matule.ui.presentation.feature.orders.ui.functions.formatYesterdayTimeSmart
import com.example.matule.ui.presentation.feature.orders.ui.functions.getDayGroupTitle
import com.example.matule.ui.presentation.feature.orders.ui.functions.getOrderTitle
import com.example.matule.ui.presentation.feature.orders.ui.functions.groupOrdersByDay
import com.example.matule.ui.presentation.feature.orders.viewmodel.OrdersScreenContract
import com.example.matule.ui.presentation.feature.orders.viewmodel.OrdersScreenViewModel
import com.example.matule.ui.presentation.shared.cart.CartItem
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.pullToRefresh.PullRefreshLayout
import com.example.matule.ui.presentation.shared.screen.EmptyScreen
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun OrdersScreen(
    navController: NavHostController,
    vm: OrdersScreenViewModel
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(OrdersScreenContract.Event.LoadOrders)
    }

    Content(
        state = state,
        onBack = {
            navController.popBackStack()
        },
        openOrderScreen = { orderId ->
            navController.navigate("${AppRouts.ORDERS_DETAIL}/$orderId")
        },
        onRefresh = {
            vm.handleEvent(OrdersScreenContract.Event.Refresh)
        },
        onDelete = { id ->
            vm.handleEvent(OrdersScreenContract.Event.DeleteOrder(id))
        },
        onUpdateOrder = { id, status ->
            vm.handleEvent(OrdersScreenContract.Event.UpdateOrderStatus(id, status))
        }
    )

}

@Composable
private fun Content(
    state: OrdersScreenContract.State,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onDelete: (Long) -> Unit,
    onUpdateOrder: (Long, OrderStatus) -> Unit,
    openOrderScreen: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.background)
            .padding(horizontal = 20.dp)
    ) {
        CustomHeader(
            backColor = Colors.block,
            text = R.string.orders,
            onBack = onBack,
            visibleNameScreen = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (state) {
            is OrdersScreenContract.State.OrdersLoaded -> {
                OrderContent(
                    orders = state.orders,
                    openOrderScreen = openOrderScreen,
                    onDelete = onDelete,
                    onUpdateOrder = onUpdateOrder
                )
            }
            is OrdersScreenContract.State.Empty -> {
                EmptyScreen(
                    modifier = Modifier.fillMaxSize(),
                    icon = R.drawable.ic_orders,
                    emptyText = R.string.empty_orders
                )
            }
            is OrdersScreenContract.State.Loading -> {
                MainLoadingScreen()
            }

            else -> {}
        }
    }
}

@Composable
fun OrderContent(
    orders: List<Order>,
    openOrderScreen: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onUpdateOrder: (Long, OrderStatus) -> Unit
) {

    val groupedOrders = groupOrdersByDay(orders)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        groupedOrders.forEach { (dayGroup, dayOrders) ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = getDayGroupTitle(dayGroup),
                    color = Colors.text,
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                    fontWeight = FontWeight.Normal,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    dayOrders.forEach { order ->
                        val totalItems = order.items.sumOf { it.quantity }
                        val firstItem = order.items.firstOrNull()
                        val timeAgo = when (dayGroup) {
                            DayGroup.TODAY -> formatSmartTimeAgo(order.createdAt)
                            DayGroup.YESTERDAY -> formatYesterdayTimeSmart(order.createdAt)
                            DayGroup.RECENTLY -> formatDaysAgoSmart(order.createdAt)
                        }


                        CartItem(
                            quantity = totalItems,
                            photoProduct = firstItem?.image ?: "",
                            timeAgo = timeAgo,
                            nameProduct = getOrderTitle(order),
                            price = firstItem?.price ?: 0.0,
                            numberOrders = order.orderNumber.toLong(),
                            openDetailScreen = {
                                openOrderScreen(order.id)
                            },
                            visibleQuantity = false,
                            onDelete = {
                                onDelete(order.id)
                            },
                            onUpdateOrder = {
                                onUpdateOrder(order.id, order.status)
                            },
                            delivery = order.delivery,
                            orders = true,
                            visibleCartComponents = false
                        )
                    }
                }
            }
        }
    }
}