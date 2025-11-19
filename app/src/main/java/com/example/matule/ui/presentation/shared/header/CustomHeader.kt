package com.example.matule.ui.presentation.shared.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Preview
@Composable
private fun PrevHeader() {
    Surface() {
        Column(
            modifier = Modifier.background(color = Colors.background),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            CustomHeader(
                text = R.string.main,
                visibleEndIcon = false,
                visibleNameScreen = false
            )
            CustomHeader(
                text = R.string.main,
                visibleEndIcon = false,
                visibleNameScreen = true
            )
            CustomHeader(
                text = R.string.favourite,
                visibleEndIcon = true,
                cardItem = 0,
                visibleNameScreen = true
            )
            CustomHeader(
                text = R.string.favourite,
                visibleEndIcon = true,
                cardItem = 1,
                visibleNameScreen = true
            )
        }
    }
}


@Composable
fun CustomHeader(
    onBack: () -> Unit = {},
    openScreen: () -> Unit = {},
    text: Int,
    cardItem: Int = 0,
    endIcon: Int = R.drawable.ic_cart,
    visibleEndIcon: Boolean = false,
    visibleNameScreen: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        CustomIconButton(
            icon = R.drawable.ic_back,
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart),
        )
        if (visibleNameScreen) {
            Text(
                text = stringResource(text),
                color = Colors.text,
                style = MatuleTypography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (visibleEndIcon) {
            CustomIconButton(
                icon = endIcon,
                onClick = openScreen,
                cardItem = cardItem,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}