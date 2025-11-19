package com.example.matule.ui.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.matule.R

val MatuleTypography: Typography
    @Composable
    get() = Typography(
        displaySmall = TextStyle(
            fontSize = 34.sp,
            lineHeight = 44.2.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontSize = 32.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontSize = 26.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontSize = 14.sp,
            lineHeight = 22.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        bodyLarge = TextStyle(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontSize = 14.sp,
            lineHeight = 16.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        ),
    )
