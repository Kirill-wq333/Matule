package com.example.matule.ui.presentation.feature.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun ProfileScreen(
    vm: ProfileScreenViewModel
) {

}

@Composable
fun ProfileContent(

) {
    Column() {
        CustomHeaderMain(
            text = R.string.profile,
            endIcon = R.drawable.ic_edit,
            openSideMenu = {  },
            openCartScreen = {  },
            padding = 8.5.dp,
            size = 8.dp,
            backColor = Colors.accent
        )
        Content()
    }
}

@Composable
private fun Content() {

}