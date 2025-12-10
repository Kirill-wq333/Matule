package com.example.matule.ui.presentation.shared.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Composable
fun EmptyScreen(
    icon: Int,
    tint: Color = Colors.hint,
    emptyText: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(96.dp)
            )
            Text(
                text = stringResource(emptyText),
                color = Colors.text,
                style = MatuleTypography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyContent(
    icon: Int,
    emptyText: Int,
    visibleIcon: Boolean = true
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (visibleIcon) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    tint = Colors.hint,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = stringResource(emptyText),
                color = Colors.text,
                style = MatuleTypography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}