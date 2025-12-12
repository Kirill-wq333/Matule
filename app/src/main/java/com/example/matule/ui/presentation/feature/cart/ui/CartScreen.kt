package com.example.matule.ui.presentation.feature.cart.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.domain.ui.presentation.feature.cart.model.CartItem
import com.example.domain.ui.presentation.feature.orders.model.Address
import com.example.domain.ui.presentation.feature.orders.model.ContactInfo
import com.example.domain.ui.presentation.feature.orders.model.CreateOrderRequest
import com.example.domain.ui.presentation.feature.orders.model.PaymentMethod
import com.example.domain.ui.presentation.feature.profile.model.UserProfile
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.cart.viewmodel.CartScreenContract
import com.example.matule.ui.presentation.feature.cart.viewmodel.CartScreenViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.cart.CartItem
import com.example.matule.ui.presentation.shared.cart.InformationSection
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.screen.EmptyScreen
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

private interface CartScreenCallback{
    fun onBack() {}
    fun openDetailScreen(id: Long) {}
    fun deleteProduct(id: Long) {}
    fun checkoutScreen(request: CreateOrderRequest) {}
    fun updateQuality(cartItemId: Long, newQuantity: Int) {}
}

@Composable
fun CartScreen(
    vm: CartScreenViewModel,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()
    val profile by vm.profile.collectAsState()

    var visibleCheckout by remember { mutableStateOf(false) }
    var visibleSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.handleEvent(CartScreenContract.Event.LoadCart)
    }

    val callback = object : CartScreenCallback{
        override fun onBack() {
            if (visibleCheckout){
             visibleCheckout = false
            } else {
                navController.popBackStack()
            }
        }

        override fun checkoutScreen(request: CreateOrderRequest) {
           vm.handleEvent(CartScreenContract.Event.CreateOrder(request))
           visibleSnackbar = true
        }

        override fun deleteProduct(id: Long) {
            vm.handleEvent(CartScreenContract.Event.RemoveItem(id))
        }

        override fun openDetailScreen(id: Long) {
            navController.navigate("${AppRouts.DETAILS}/$id")
        }

        override fun updateQuality(cartItemId: Long, newQuantity: Int) {
            vm.handleEvent(CartScreenContract.Event.UpdateQuantity(cartItemId, newQuantity))
        }
    }

    CartContent(
        callback = callback,
        state = state,
        profile = profile,
        visibleCheckout = visibleCheckout,
        onShowCheckoutForm = { visibleCheckout = true },
        visibleSnackBar = visibleSnackbar
    )

    AnimatedVisibility(
       visible = visibleSnackbar,
       enter = fadeIn() + scaleIn(tween(150)),
       exit = fadeOut() + scaleOut(tween(150))
    ) {
        CardCongratulations {
            navController.navigate(AppRouts.MAIN)
        }
    }
}

@Composable
private fun CartContent(
    visibleCheckout: Boolean,
    visibleSnackBar: Boolean,
    profile: UserProfile,
    onShowCheckoutForm: () -> Unit,
    state: CartScreenContract.State,
    callback: CartScreenCallback,
) {
    Column(
        modifier = Modifier
            .background(color = if (visibleSnackBar) Colors.background.copy(.2f) else Colors.background)
            .blur(if (visibleSnackBar) 4.dp else 0.dp)
            .fillMaxSize()
    ) {
        CustomHeader(
            text = R.string.cart,
            visibleNameScreen = true,
            modifier = Modifier.padding(horizontal = 20.dp),
            onBack = callback::onBack
        )
        Spacer(modifier = Modifier.height(if (visibleCheckout) 46.dp else 16.dp))
        when(state) {
            is CartScreenContract.State.Loaded -> {
                Cart(
                    state = state,
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
                        if (quality > 1) {
                            callback.updateQuality(id, quality - 1)
                        }
                    },
                    profile = profile,
                    onShowCheckoutForm = onShowCheckoutForm,
                    onCreateOrder = {
                        callback.checkoutScreen(it)
                    },
                    visibleCheckout = visibleCheckout
                )
            }
            is CartScreenContract.State.Loading -> {
                MainLoadingScreen()
            }
            else -> {}
        }
    }
}

@Composable
fun Cart(
    state: CartScreenContract.State.Loaded,
    profile: UserProfile,
    visibleCheckout: Boolean,
    openDetailScreen: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onShowCheckoutForm: () -> Unit,
    onCreateOrder: (CreateOrderRequest) -> Unit,
    onPlusQuantity: (Long, Int) -> Unit,
    onMinusQuantity: (Long, Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedVisibility(
            visible = !visibleCheckout,
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Top
            ) + fadeOut(animationSpec = tween(700))
        ) {
            Content(
                cartProduct = state.items,
                openDetailScreen = openDetailScreen,
                onDelete = onDelete,
                onPlusQuantity = onPlusQuantity,
                onMinusQuantity = onMinusQuantity
            )
        }
        AnimatedVisibility(
            visible = visibleCheckout,
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Top
            ) + fadeOut(animationSpec = tween(700))
        ) {
            InformationSection(
                address = profile.address,
                city = profile.city,
                country = profile.country,
                postalCode = profile.postalCode,
                phone = profile.phone,
                visibleEndIcon = true,
                email = profile.email
            )
        }

        BottomBarCart(
            subtotal = state.subtotal,
            delivery = state.delivery,
            totalCost = state.totalPrice,
            openCheckoutScreen = {
                if (!visibleCheckout) {
                    onShowCheckoutForm()
                } else {
                    val request = CreateOrderRequest(
                        contactInfo = ContactInfo(
                            firstName = profile.firstName,
                            lastName = profile.lastName,
                            phone = profile.phone ?: "",
                            email = profile.email
                        ),
                        address = Address(
                            country = profile.country ?: "",
                            city = profile.city ?: "",
                            street = profile.address ?: "",
                            postalCode = profile.postalCode,
                            apartment = ""
                        ),
                        paymentMethod = PaymentMethod.fromString("Card"),
                    )
                    onCreateOrder(request)
                }
            },
            visibleCheckout = visibleCheckout
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
            text = getProductsCountText(cartProduct.sumOf { it.quantity }),
            color = Colors.text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (cartProduct.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                cartProduct.forEach { cart ->

                    CartItem(
                        timeAgo = "",
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
        } else {
            EmptyScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                icon = R.drawable.ic_cart,
                emptyText = R.string.empty_cart
            )
        }
    }
}

@Composable
fun BottomBarCart(
    subtotal: Double,
    visibleCheckout: Boolean,
    delivery: Double,
    totalCost: Double,
    openCheckoutScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (visibleCheckout) Colors.background else Colors.block)
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
            text = if (visibleCheckout) R.string.btn_confirm else R.string.btn_checkout,
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

@Composable
fun CardCongratulations(
    openMainScreen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 20.dp, end = 20.dp, bottom = 153.dp
                )
                .background(color = Colors.block, RoundedCornerShape(20.dp))
                .padding(horizontal = 40.dp, vertical = 49.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFFDFEFFF), shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(R.drawable.congratulations),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(24.dp)
                            .size(86.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.checkout_popup),
                    color = Colors.text,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                )
            }
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.btn_back_to_shopping,
                onClick = openMainScreen
            )
        }
    }
}

fun getProductsCountText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "$count товар"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "$count товара"
        else -> "$count товаров"
    }
}