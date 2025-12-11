package com.example.matule.ui.presentation.shared.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
    icon: Int,
    shape: Shape = CircleShape,
    backColor: Color = Colors.block,
    onClick: () -> Unit = {},
    cardItem: Int = 0,
    padding: Dp = 10.dp,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp
) {
    Box(
        modifier = modifier
            .background(
                color = backColor,
                shape = shape
            )
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = modifierIcon
                .padding(padding)
                .size(size)
        )
        if (cardItem > 0) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 3.dp, end = 2.dp)
                    .background(
                        color = Colors.red,
                        shape = shape
                    )
            )
        }
    }
}