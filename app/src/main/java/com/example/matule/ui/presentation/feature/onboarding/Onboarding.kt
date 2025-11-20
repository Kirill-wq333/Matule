package com.example.matule.ui.presentation.feature.onboarding

data class Onboarding(
    val heading: Int,
    val underHeading: Int,
    val visibleHeading: Boolean,
    val visibleDownHeadingAndUnderHeading: Boolean,
    val image: Int,
    val appRouts: String = "",
)
