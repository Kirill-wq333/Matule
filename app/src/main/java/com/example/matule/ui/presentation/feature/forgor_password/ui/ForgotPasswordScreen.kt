package com.example.matule.ui.presentation.feature.forgor_password.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.feature.auth.ui.TitleAndSubTitleAuth
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.text.TextFieldWithTrailingIcon
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Composable
fun ForgotPassword(
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var visiblePopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .blur(radius = if (visiblePopup) 50.dp else 0.dp,)
            .background(color = if (visiblePopup) Colors.block.copy(0.25f) else Colors.block)
            .padding(vertical = 23.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(
            backColor = Colors.background,
            onBack = {
                navController.popBackStack()
            },
            text = R.string.forgot_password
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            TitleAndSubTitleAuth(
                heading = R.string.forgot_password,
                underHeading = R.string.forgot_password_under_heading
            )
            TextFieldWithTrailingIcon(
                modifier = Modifier.fillMaxWidth(),
                query = email,
                onTextChange = {
                    email = it
                },
                placeholder = "email@example.com"
            )
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = R.string.btn_sent,
                onClick = {
                    visiblePopup =true
                }
            )
        }
    }
    AnimatedVisibility(
        visible = visiblePopup,
        enter = fadeIn() + scaleIn(tween(150)),
        exit = fadeOut() + scaleOut(tween(150))
    ) {
        Popup()
    }
}

@Composable
fun Popup() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(color = Colors.block, shape = RoundedCornerShape(16.dp))
                .padding(vertical = 30.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomIconButton(
                backColor = Colors.accent,
                tint = Colors.block,
                icon = R.drawable.ic_email_otp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.popup_heading),
                color = Colors.text,
                textAlign = TextAlign.Center,
                style = MatuleTypography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.popup_under_heading),
                color = Colors.text,
                textAlign = TextAlign.Center,
                style = MatuleTypography.bodyLarge
            )
        }
    }
}