package com.example.matule.ui.presentation.shared.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun CheckboxPrivacyPolice(
    isSelected: Boolean
){
    val backColor = if (isSelected) Colors.accent else Colors.background

    val colorState by animateColorAsState(
        targetValue = backColor,
        animationSpec = tween(700)
    )
    Box(
        modifier = Modifier.background(color = colorState, shape = RoundedCornerShape(6.dp))
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_policy_check),
            contentDescription = null,
            modifier = Modifier.padding(4.dp)
        )
    }
}
