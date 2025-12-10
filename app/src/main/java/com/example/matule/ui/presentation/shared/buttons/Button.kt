package com.example.matule.ui.presentation.shared.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Preview
@Composable
private fun ColumnCustomButtons() {
    Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
        CustomButtonForBuy(
            modifier = Modifier.fillMaxWidth(),
        )
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.btn_save,
            color = Colors.text,
            backgroundColor = Colors.block
        )
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.btn_back_to_shopping
        )
    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    color: Color = Colors.block,
    backgroundColor: Color = Colors.accent,
    text: Int
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = stringResource(text),
            color = color,
            style = MatuleTypography.titleSmall,
            modifier = Modifier.padding(vertical = 14.dp)
        )
    }
}

@Composable
fun CustomButtonForBuy(
    modifier: Modifier = Modifier,
    visibleInCart: Boolean = false,
    onClick: () -> Unit = {},
    color: Color = Colors.background,
    backgroundColor: Color = Colors.accent
) {

    val text = if (visibleInCart) R.string.btn_added else R.string.btn_add_to_cart

    Box(
        modifier = modifier
            .clickable(onClick = {
                    if (!visibleInCart) {
                        onClick()
                    }
                }
            )
            .background(color = backgroundColor, shape = RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_cart),
            contentDescription = null,
            tint = Colors.block,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        )
        Text(
            text = stringResource(text),
            color = color,
            style = MatuleTypography.titleSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 14.dp)
        )
    }
}