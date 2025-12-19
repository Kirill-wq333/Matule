package com.example.matule.ui.presentation.feature.onboarding.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.onboarding.Onboarding
import com.example.matule.ui.presentation.feature.onboarding.viewmodel.OnboardingViewModel
import com.example.matule.ui.presentation.mock.Mock
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography
import kotlinx.coroutines.launch

private interface OnboardingScreenCallback{
    fun openAuthScreen() {}
}


@Composable
fun OnboardingScreen(
    vm: OnboardingViewModel,
    navController: NavHostController
) {
    val onboarding = Mock.demoOnboarding

    val callback = object : OnboardingScreenCallback{
        override fun openAuthScreen() {
            vm.completeOnboarding()
            navController.navigate(AppRouts.AUTH)
        }
    }


    LaunchedEffect(vm) {
        vm.onOnboardingCompleted.collect { completed ->
            if (completed) {
                val token = vm.getToken()
                if (token != null) {
                    navController.navigate(AppRouts.MAIN) {
                        popUpTo(AppRouts.ONBOARDING) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppRouts.AUTH) {
                        popUpTo(AppRouts.ONBOARDING) { inclusive = true }
                    }
                }
            }
        }
    }

    OnboardingContent(
        onboarding = onboarding,
        callback = callback
    )
}

@Composable
private fun OnboardingContent(
    onboarding: List<Onboarding>,
    callback: OnboardingScreenCallback
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { onboarding.size }
    )

    val scope = rememberCoroutineScope()

    val animatedAccentColor by animateColorAsState(
        targetValue = when (pagerState.currentPage) {
            0 -> Colors.accent
            1 -> Color(0xFF44A9DC)
            2 -> Colors.accent
            else -> Colors.accent
        },
        animationSpec = tween(durationMillis = 500)
    )

    val animatedDisableColor by animateColorAsState(
        targetValue = Colors.disable,
        animationSpec = tween(durationMillis = 500)
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = true
    ) { page ->
        val currentItem = onboarding.getOrNull(page) ?: return@HorizontalPager

        val linearGradient = if (currentItem.visibleDownHeadingAndUnderHeading) {
            Brush.linearGradient(
                colors = listOf(animatedAccentColor, Color(0xFF44A9DC), animatedDisableColor),
                start = Offset(0f, 0f),
                end = Offset.Infinite
            )
        } else {
            Brush.linearGradient(
                colors = listOf(animatedAccentColor, animatedDisableColor),
                start = Offset(0f, 0f),
                end = Offset.Infinite
            )
        }

        Column(
            modifier = Modifier
                .background(linearGradient)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentItem.visibleHeading) {
                Text(
                    text = stringResource(currentItem.heading),
                    color = Colors.block,
                    textAlign = TextAlign.Center,
                    style = MatuleTypography.headlineMedium,
                    modifier = Modifier.padding(start = 50.dp, end = 58.dp, top = 29.dp)
                )
                Spacer(modifier = Modifier.height(130.dp))
            }
            Content(
                onboarding = currentItem,
                activeCircle = page,
                openAuthScreen = callback::openAuthScreen,
                nextOnboarding = {
                    scope.launch {
                        val nextPage = (page + 1) % onboarding.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            )
        }
    }
}

@Composable
private fun Content(
    onboarding: Onboarding,
    activeCircle: Int,
    openAuthScreen: () -> Unit,
    nextOnboarding: () -> Unit
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageAndMain(
            onboarding = onboarding,
            activeCircle = activeCircle
        )
        val isLastScreen = activeCircle == 2

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 36.dp),
            text = if (activeCircle == 2) R.string.btn_start else R.string.btn_next,
            color = Colors.text,
            backgroundColor = Colors.block,
            onClick = {
                if (isLastScreen) {
                    openAuthScreen()
                } else {
                    nextOnboarding()
                }
            }
        )
    }
}

@Composable
fun ImageAndMain(
    onboarding: Onboarding,
    activeCircle: Int
) {
    val spacerImageAndHeadingUnderHeading =
        if (onboarding.visibleDownHeadingAndUnderHeading) 60.dp
        else 26.dp

    val paddingImage =
        if (onboarding.visibleDownHeadingAndUnderHeading) 37.dp
        else 0.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingImage),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(onboarding.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(spacerImageAndHeadingUnderHeading))
        if (onboarding.visibleDownHeadingAndUnderHeading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(onboarding.heading),
                    color = Colors.block,
                    textAlign = TextAlign.Center,
                    style = MatuleTypography.displaySmall
                )
                Text(
                    text = stringResource(onboarding.underHeading),
                    color = Colors.subTextLight,
                    textAlign = TextAlign.Center,
                    style = MatuleTypography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
        ThreeCircular(
            activeCircle = activeCircle
        )
    }
}

@Composable
fun ThreeCircular(
    activeCircle: Int = 0
) {

    val smallWidth = 28.dp
    val smallHeight = 5.dp
    val largeWidth = 43.dp
    val largeHeight = 5.dp

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val isActive = index == activeCircle

            val targetWidth by animateDpAsState(
                targetValue = if (isActive) largeWidth else smallWidth,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "circle_width_$index"
            )

            val targetHeight by animateDpAsState(
                targetValue = if (isActive) largeHeight else smallHeight,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "circle_height_$index"
            )

            Box(
                modifier = Modifier
                    .background(
                        color = if (isActive) Colors.block else Colors.disable,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .size(width = targetWidth, height = targetHeight)
            )
        }
    }
}