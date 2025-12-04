package com.example.matule.ui.presentation.feature.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenContract
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenViewModel
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.shared.text.CustomTextField
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Composable
fun ProfileScreen(
    vm: ProfileScreenViewModel,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()

    ProfileContent(
        openSideMenu = { navController.navigate(AppRouts.SIDE_MENU) },
        state = state
    )
}

@Composable
fun ProfileContent(
    state: ProfileScreenContract.State,
    openSideMenu: () -> Unit
) {
    var visibleEditingScreen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.block)
            .padding(
                start = 18.dp, end = 17.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        CustomHeaderMain(
            text = R.string.profile,
            endIcon = R.drawable.ic_edit,
            openSideMenu = openSideMenu,
            tintEndIcon = Colors.block,
            openCartScreen = {
                visibleEditingScreen = true
            },
            padding = 8.5.dp,
            visibleCosmeticIcon = false,
            style = MatuleTypography.bodyLarge,
            size = 15.dp,
            backColor = Colors.accent
        )
        Spacer(modifier = Modifier.height(45.dp))
        when(state) {
            is ProfileScreenContract.State.ProfileLoaded -> {
                Content(
                    photo = state.profile.avatar,
                    firstName = state.profile.firstName,
                    lastName = state.profile.lastName,
                    phone = state.profile.phone,
                    country = state.profile.country,
                    city = state.profile.city,
                    address = state.profile.address,
                    postalCode = state.profile.postalCode,
                    dateOfBirth = state.profile.dateOfBirth,
                    email = state.profile.email,
                    visibleEditingScreen = visibleEditingScreen
                )
            }
            is ProfileScreenContract.State.Loading -> {
                MainLoadingScreen()
            }
            else -> {}
        }
    }
}

@Composable
private fun Content(
    photo: String?,
    firstName: String,
    lastName: String?,
    phone: String?,
    country: String?,
    city: String?,
    visibleEditingScreen: Boolean,
    address: String?,
    postalCode: String?,
    dateOfBirth: String?,
    email: String
) {
    var firstNameTextField by remember { mutableStateOf(firstName) }
    var lastNameTextField by remember { mutableStateOf(lastName) }
    var phoneTextField by remember { mutableStateOf(phone) }
    var countryTextField by remember { mutableStateOf(country) }
    var cityTextField by remember { mutableStateOf(city) }
    var addressTextField by remember { mutableStateOf(address) }
    var postalCodeTextField by remember { mutableStateOf(postalCode) }
    var dateOfBirthTextField by remember { mutableStateOf(dateOfBirth) }
    var emailTextField by remember { mutableStateOf(email) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
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
        Column(
            modifier = Modifier
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = firstNameTextField,
                onTextChange = {
                    firstNameTextField = it
                },
                label = R.string.name,
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = lastNameTextField ?: "",
                onTextChange = {
                    lastNameTextField = it
                },
                label = R.string.last_name,
                placeholder = stringResource(R.string.last_name)
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = "$countryTextField, $cityTextField, $addressTextField, $postalCodeTextField",
                onTextChange = {
//                    if (cityTextField!!.isNotEmpty() ||
//                        countryTextField!!.isNotEmpty() ||
//                        addressTextField!!.isNotEmpty() ||
//                        postalCodeTextField!!.isNotEmpty()
//                    ) {
//
//                    }
                },
                label = R.string.address,
                placeholder = "Область, город, адрес, номер дома"
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = phoneTextField ?: "",
                onTextChange = {
                    phoneTextField = it
                },
                label = R.string.phone
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = dateOfBirthTextField ?: "",
                onTextChange = {
                    dateOfBirthTextField = it
                },
                label = R.string.date_of_birth,
                placeholder = "День рождение"
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = emailTextField,
                onTextChange = {
                    emailTextField = it
                },
                label = R.string.email
            )
        }
    }
}

@Composable
fun QrCode() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {})
            .background(
                color = Colors.background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 2.dp)
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
                fontWeight = FontWeight.Normal,
                modifier = Modifier.rotate(-90f)
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
            color = Colors.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
    }
}