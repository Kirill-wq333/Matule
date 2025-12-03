package com.example.matule.ui.presentation.feature.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.text.CustomTextField
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun ProfileScreen(
    vm: ProfileScreenViewModel,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()

    ProfileContent(
        openSideMenu = { navController.navigate(AppRouts.SIDE_MENU) }
    )
}

@Composable
fun ProfileContent(
    openSideMenu: () -> Unit
) {
    Column() {
        CustomHeaderMain(
            text = R.string.profile,
            endIcon = R.drawable.ic_edit,
            openSideMenu = openSideMenu,
            openCartScreen = {  },
            padding = 8.5.dp,
            size = 8.dp,
            backColor = Colors.accent
        )
        Content(
            photo = "",
            firstName = "",
            lastName = ""
        )
    }
}

@Composable
private fun Content(
    photo: String?,
    firstName: String,
    lastName: String?,
) {
    Column(
        modifier = Modifier.padding(
            start = 18.dp, end = 17.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AvatarWithNameUser(
            photo = photo,
            firstName = firstName,
            lastName = lastName
        )
        Spacer(modifier = Modifier.height(38.dp))
        QrCode()
        Spacer(modifier = Modifier.height(19.dp))
        Column() {
//            CustomTextField()
//            CustomTextField()
//            CustomTextField()
//            CustomTextField()
        }
    }
}

@Composable
fun QrCode() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Colors.background,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.open),
                color = Colors.text,
                fontSize = 12.sp,
                lineHeight = 22.sp,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                fontWeight = FontWeight.Normal
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(51.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.qr_code),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AvatarWithNameUser(
    photo: String?,
    firstName: String,
    lastName: String?
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(96.dp)
        ) {
            if (!photo.isNullOrEmpty()) {
                AsyncImage(
                    model = photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }else{
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                )
            }
        }
        Text(
            text = "$firstName $lastName",
            color = Colors.block,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
    }
}