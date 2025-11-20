package com.example.matule.ui.presentation.mock

import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.onboarding.Onboarding

object Mock {

    val demoOnboarding = listOf(
        Onboarding(
            heading = R.string.onboarding_heading,
            underHeading = R.string.onboarding_under_heading2,
            visibleHeading = true,
            image = R.drawable.image_1,
            visibleDownHeadingAndUnderHeading = false,
        ),
        Onboarding(
            heading = R.string.onboarding_heading2,
            underHeading = R.string.onboarding_under_heading2,
            visibleHeading = false,
            image = R.drawable.image_2,
            visibleDownHeadingAndUnderHeading = true,
        ),
        Onboarding(
            heading = R.string.onboarding_heading3,
            underHeading = R.string.onboarding_under_heading3,
            visibleHeading = false,
            image = R.drawable.image_3,
            appRouts = AppRouts.AUTH,
            visibleDownHeadingAndUnderHeading = true,
        )
    )

}