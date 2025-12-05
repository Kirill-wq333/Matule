package com.example.matule.ui.presentation.feature.sidemenu.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.sidemenu.viewmodel.SideMenuScreenContract
import com.example.matule.ui.presentation.feature.sidemenu.viewmodel.SideMenuScreenViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography
import kotlinx.coroutines.delay

@Preview
@Composable
private fun PreviewContent() {
   SideMenuScreen(onBack = {})
}


@Composable
fun SideMenuScreen(
    vm: SideMenuScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    navController: NavHostController = rememberNavController()
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(SideMenuScreenContract.Event.LoadUserProfile)
    }

    val sideMenu = listOf(
        SideMenu(
            route = AppRouts.PROFILE,
            label = R.string.profile,
            icon = R.drawable.ic_profile
        ),
        SideMenu(
            route = AppRouts.CART,
            label = R.string.cart,
            icon = R.drawable.ic_cart
        ),
        SideMenu(
            route = AppRouts.FAVOURITE,
            label = R.string.favourite,
            icon = R.drawable.ic_favorite
        ),
        SideMenu(
            route = AppRouts.ORDERS,
            label = R.string.orders,
            icon = R.drawable.ic_orders
        ),
        SideMenu(
            route = AppRouts.NOTIFICATION,
            label = R.string.notifications,
            icon = R.drawable.ic_notification,
            notificationItem = 1
        ),
        SideMenu(
            route = "",
            label = R.string.setting,
            icon = R.drawable.ic_settings
        )
    )

    var visibleCard by remember { mutableStateOf(false) }
    var visibleCard2 by remember { mutableStateOf(false) }
    val offsetX1 by animateDpAsState(
        targetValue = if (visibleCard) 0.dp else (-100).dp,
        animationSpec = tween(durationMillis = 1500, easing = EaseOut)
    )

    val offsetX2 by animateDpAsState(
        targetValue = if (visibleCard2) 162.dp else 300.dp,
        animationSpec = tween(durationMillis = 1500, easing = EaseOut)
    )

    LaunchedEffect(Unit) {
        visibleCard = true
        delay(200)
        visibleCard2 = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -30f) {
                        onBack()
                    }
                }
            }
    ) {
        Content(
            modifier = modifier,
            visibleCard = visibleCard,
            visibleCard2 = visibleCard2,
            offsetX1 = offsetX1,
            offsetX2 = offsetX2,
            sideMenu = sideMenu,
            navController = navController,
            onEvent = vm::handleEvent,
            state = state
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    visibleCard: Boolean,
    state: SideMenuScreenContract.State,
    visibleCard2: Boolean,
    offsetX2: Dp,
    offsetX1: Dp,
    sideMenu: List<SideMenu>,
    onEvent: (SideMenuScreenContract.Event) -> Unit,
    navController: NavHostController,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Colors.accent)
    ) {
        AnimatedVisibility(
            visible = visibleCard,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { -it })
        ) {
            SideMenuContent(
                modifier = Modifier
                    .offset(x = offsetX1)
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 20.dp),
                onEvent = onEvent,
                sideMenu = sideMenu,
                navController = navController,
                state = state
            )
        }
        AnimatedVisibility(
            visible = visibleCard2,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { it })
        ) { }
        Box(
            modifier = Modifier
                .size(278.dp, 602.dp)
                .offset(x = offsetX2)
                .rotate(-3.43f)
                .shadow(
                    elevation = 21.dp,
                    clip = false,
                    shape = RoundedCornerShape(25.dp)
                )
                .clip(RoundedCornerShape(25.dp))
                .align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(R.drawable.home_screen),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}



@Composable
private fun SideMenuContent(
    modifier: Modifier = Modifier,
    sideMenu: List<SideMenu>,
    state: SideMenuScreenContract.State,
    onEvent: (SideMenuScreenContract.Event) -> Unit,
    navController: NavHostController,
) {
    Column(
        modifier = modifier
    ) {
        when(state) {
            is SideMenuScreenContract.State.ProfileLoaded -> {
                PhotoAndNameWithLastName(
                    photo = state.profile.avatar,
                    firstName = state.profile.firstName,
                    lastName = state.profile.lastName ?: ""
                )
            }
            is SideMenuScreenContract.State.Loading -> {
                Box {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(50.dp)
                    )
                }
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(58.dp))
        Block(
            sideMenu = sideMenu,
            onEvent = onEvent,
            navController = navController
        )
    }
}

@Composable
fun PhotoAndNameWithLastName(
    photo: String?,
    firstName: String,
    lastName: String,
) {
    Column(
        modifier = Modifier.padding(start = 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(96.dp)
        ) {
            if (!photo.isNullOrEmpty()) {
                AsyncImage(
                    model = photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }else{
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                )
            }
        }
        Text(
            text = "$firstName $lastName",
            color = Colors.block,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
    }
}

@Composable
fun Block(
    onEvent: (SideMenuScreenContract.Event) -> Unit,
    sideMenu: List<SideMenu>,
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        sideMenu.forEach { item ->
            BlockItem(
                text = item.label,
                icon = item.icon,
                notificationItem = item.notificationItem,
                onClick = { navController.navigate(item.route) }
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Colors.background.copy(0.23f)
        )
        BlockItem(
            text = R.string.sign_out,
            icon = R.drawable.ic_logout,
            onClick = {
                onEvent(SideMenuScreenContract.Event.Logout)
                navController.navigate(AppRouts.AUTH)
            }
        )
    }
}

@Composable
fun BlockItem(
    icon: Int,
    text: Int,
    onClick: () -> Unit,
    notificationItem: Int = 0
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        CustomIconButton(
            icon = icon,
            backColor = Color.Unspecified,
            tint = Colors.block,
            padding = 0.dp,
            cardItem = notificationItem
        )
        Text(
            text = stringResource(text),
            color = Colors.block,
            style = MatuleTypography.bodyLarge
        )
    }
}