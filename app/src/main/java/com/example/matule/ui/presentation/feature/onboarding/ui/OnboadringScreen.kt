package com.example.matule.ui.presentation.feature.onboarding.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.onboarding.Onboarding
import com.example.matule.ui.presentation.mock.Mock
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

private interface OnboardingScreenCallback{
    fun openAuthScreen() {}
}


@Composable
fun OnboardingScreen(
    navController: NavHostController
) {
    val onboarding = Mock.demoOnboarding

    val callback = object : OnboardingScreenCallback{
        override fun openAuthScreen() {
            navController.navigate(AppRouts.AUTH)
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
    var activeCircle by remember { mutableIntStateOf(0) }
    val item = onboarding.getOrNull(activeCircle) ?: return

    val linearGradient =
        if (item.visibleDownHeadingAndUnderHeading)Brush.linearGradient(
        listOf(Colors.accent, Color(0xFF44A9DC), Colors.disable))
        else Brush.linearGradient(listOf(Colors.accent, Colors.disable))

    Column(
        modifier = Modifier
            .background(linearGradient)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (item.visibleHeading) {
            Text(
                text = stringResource(item.heading),
                color = Colors.block,
                textAlign = TextAlign.Center,
                style = MatuleTypography.headlineMedium,
                modifier = Modifier.padding(start = 50.dp, end = 58.dp, top = 29.dp)
            )
            Spacer(modifier = Modifier.height(130.dp))
        }
        Content(
            onboarding = item,
            activeCircle = activeCircle,
            openAuthScreen = callback::openAuthScreen,
            nextOnboarding = { activeCircle = (activeCircle + 1) % 3 }
        )
    }
}

@Composable
private fun Content(
    onboarding: Onboarding,
    activeCircle: Int,
    openAuthScreen: () -> Unit,
    nextOnboarding: () -> Unit
) {

    val spacerImageAndHeadingUnderHeading =
        if (onboarding.visibleDownHeadingAndUnderHeading) 60.dp
        else 26.dp

    val spacerCircle =
        if (onboarding.visibleDownHeadingAndUnderHeading) 95.dp
        else 136.dp

    val paddingImage =
        if (onboarding.visibleDownHeadingAndUnderHeading) 37.dp
        else 0.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageAndMain(
            onboarding = onboarding,
            activeCircle = activeCircle
        )
//        Spacer(modifier = Modifier.height(spacerCircle))
        if (onboarding.visibleDownHeadingAndUnderHeading) {
            val isLastScreen = activeCircle == 2

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 36.dp ),
                text = R.string.btn_next,
                color = Colors.text,
                backgroundColor = Colors.block,
                onClick = {
                    if (isLastScreen){
                        openAuthScreen()
                    }
                    else{
                        nextOnboarding()
                    }
                }
            )
        }
        else{
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 36.dp ),
                text = R.string.btn_start,
                color = Colors.text,
                backgroundColor = Colors.block,
                onClick = nextOnboarding
            )
        }
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