package com.example.matule.ui.presentation.feature.profile.ui

import android.app.Activity
import android.view.Window
import android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
import android.view.WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.ui.presentation.feature.profile.model.UserProfile
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenContract
import com.example.matule.ui.presentation.feature.profile.viewmodel.ProfileScreenViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.mask.rememberMaskVisualTransformation
import com.example.matule.ui.presentation.shared.pullToRefresh.PullRefreshLayout
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
    val profile by vm.profile.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(ProfileScreenContract.Event.LoadProfile)
    }

    ProfileContent(
        openSideMenu = { navController.navigate(AppRouts.SIDE_MENU) },
        state = state,
        vm = vm,
        profile = profile
    )
}

@Composable
fun ProfileContent(
    profile: UserProfile,
    vm: ProfileScreenViewModel,
    state: ProfileScreenContract.State,
    openSideMenu: () -> Unit
) {

    var firstName by remember(profile) {
        mutableStateOf(profile.firstName)
    }
    var lastName by remember(profile) {
        mutableStateOf(profile.lastName ?: "")
    }
    var phone by remember(profile) {
        mutableStateOf(profile.phone ?: "")
    }
    var country by remember(profile) {
        mutableStateOf(profile.country ?: "")
    }
    var city by remember(profile) {
        mutableStateOf(profile.city ?: "")
    }
    var address by remember(profile) {
        mutableStateOf(profile.address ?: "")
    }
    var postalCode by remember(profile) {
        mutableStateOf(profile.postalCode ?: "")
    }
    var email by remember(profile) {
        mutableStateOf(profile.email)
    }


    val hasChanges by remember(
        profile,
        firstName,
        lastName,
        phone,
        country,
        city,
        address,
        postalCode,
        email
    ) {
        derivedStateOf {

            firstName != profile.firstName ||
                    lastName != profile.lastName ||
                    phone != profile.phone ||
                    country != profile.country ||
                    city != profile.city ||
                    address != profile.address ||
                    postalCode != profile.postalCode ||
                    email != profile.email
        }
    }

    var visibleEditingScreen by remember { mutableStateOf(false) }
    var qrCode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.block)
            .padding(
                start = 18.dp, end = 17.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        AnimatedVisibility(
            visible = !visibleEditingScreen,
            enter = fadeIn(tween(1700)) + slideInHorizontally(tween(700)) { it },
            exit = fadeOut(tween(700)) + slideOutHorizontally(tween(700)) { it }
        ) {
            Column {
                CustomHeaderMain(
                    text = R.string.profile,
                    endIcon = R.drawable.ic_edit,
                    openSideMenu = openSideMenu,
                    tintEndIcon = Colors.block,
                    style = MatuleTypography.bodyLarge,
                    openCartScreen = {
                        visibleEditingScreen = true
                    },
                    padding = 8.5.dp,
                    visibleCosmeticIcon = false,
                    size = 15.dp,
                    backColor = Colors.accent
                )
                Spacer(modifier = Modifier.height(45.dp))
            }
        }

        AnimatedVisibility(
            visible = visibleEditingScreen && hasChanges,
            enter = fadeIn(tween(1700)) + slideInVertically(tween(700)) { -it },
            exit = fadeOut(tween(700)) + slideOutVertically(tween(700)) { -it }
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                CustomButton(
                    text = R.string.btn_save,
                    onClick = {
                        vm.handleEvent(
                            ProfileScreenContract.Event.UpdateProfileFields(
                                email = email,
                                firstName = firstName,
                                lastName = lastName,
                                phone = phone,
                                country = country,
                                city = city,
                                address = address,
                                postalCode = postalCode,
                            )
                        )
                        visibleEditingScreen = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 68.dp, end = 59.dp),
                )
                Spacer(modifier = Modifier.height(43.dp))
            }
        }
        when (state) {
            is ProfileScreenContract.State.ProfileLoaded -> {
                Content(
                    photo = profile.avatar ?: "",
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    country = country,
                    city = city,
                    address = address,
                    postalCode = postalCode,
                    email = email,
                    visibleEditingScreen = visibleEditingScreen,
                    openQrCodeScreen = { qrCode = true },
                    onEmailChange = { email = it },
                    onFirstNameChange = { firstName = it },
                    onLastNameChange = { lastName = it },
                    onPhoneChange = { phone = it },
                    onAddressChange = { address = it },
                    onCityChange = { city = it },
                    onCountryChange = { country = it },
                    onPostalCodeChange = { postalCode = it }
                )
            }
            is ProfileScreenContract.State.Loading -> {
                MainLoadingScreen()
            }
            else -> {}
        }
    }
    AnimatedVisibility(
        visible = qrCode,
        enter = fadeIn() + scaleIn(tween(150)),
        exit = fadeOut() + scaleOut(tween(150))
    ) {
        QrCodeScreen(
            onClose = { qrCode = false }
        )
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
    openQrCodeScreen: () -> Unit,
    address: String?,
    postalCode: String?,
    email: String,
    onEmailChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPostalCodeChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
) {

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

        AnimatedVisibility(
            visible = visibleEditingScreen,
            enter = fadeIn(tween(700)),
            exit = fadeOut(tween(700))
        ) {
            Column {
                Spacer(modifier = Modifier.height(11.dp))
                Text(
                    text = stringResource(R.string.change_your_profile_photo),
                    color = Colors.accent,
                    style = MatuleTypography.bodySmall
                )
                Spacer(modifier = Modifier.height(21.dp))
            }
        }

        AnimatedVisibility(
            visible = !visibleEditingScreen,
            enter = fadeIn(tween(700)),
            exit = fadeOut(tween(700))
        ) {
            Column {
                Spacer(modifier = Modifier.height(38.dp))
                QrCode(
                    openQrCodeScreen = openQrCodeScreen
                )
                Spacer(modifier = Modifier.height(19.dp))
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = firstName,
                onTextChange = {
                    onFirstNameChange(it)
                },
                label = R.string.name,
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = lastName ?: "",
                onTextChange = {
                    onLastNameChange(it)
                },
                label = R.string.last_name,
                placeholder = if (visibleEditingScreen) stringResource(R.string.last_name) else ""
            )
            if (visibleEditingScreen) {
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    query = country ?: "",
                    onTextChange = onCountryChange,
                    label = R.string.country,
                    placeholder = stringResource(R.string.country)
                )
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    query = city ?: "",
                    onTextChange = onCityChange,
                    label = R.string.city,
                    placeholder = stringResource(R.string.city)
                )
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    query = address ?: "",
                    onTextChange = onAddressChange,
                    label = R.string.address,
                    placeholder = stringResource(R.string.address)
                )
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    query = postalCode ?: "",
                    onTextChange = onPostalCodeChange,
                    label = R.string.postal_code,
                    placeholder = stringResource(R.string.postal_code)
                )
            } else {
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    query = if (
                        country.isNullOrEmpty() ||
                        city.isNullOrEmpty() ||
                        address.isNullOrEmpty() ||
                        postalCode.isNullOrEmpty()
                        ) ""
                    else "$country, $city, $address, $postalCode",
                    onTextChange = {},
                    label = R.string.address,
                    placeholder = "",
                )
            }
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = phone ?: "",
                visualTransformation = rememberMaskVisualTransformation("+#(###)###-##-##"),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                onTextChange = { newText ->
                    val digitsOnly = newText.filter { it.isDigit() }

                    if (digitsOnly.length <= 11) {
                        onPhoneChange(newText)
                    }
                },
                placeholder = if(visibleEditingScreen) "+7 (999) 999-00-00" else "",
                label = R.string.phone
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = !visibleEditingScreen,
                query = email,
                onTextChange = {
                    onEmailChange(it)
                },
                label = R.string.email
            )
        }
    }
}

@Composable
fun QrCode(
    openQrCodeScreen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = openQrCodeScreen)
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


@Composable
fun QrCodeScreen(
    onClose: () -> Unit
) {

    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val initialBrightness = window.attributes.screenBrightness
        window.setBrightness(BRIGHTNESS_OVERRIDE_FULL)

        onDispose {
            window.setBrightness(initialBrightness)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.background.copy(0.8f))
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .border(width = 1.dp, color = Colors.text, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.qr_code),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }

}

fun Window.setBrightness(value: Float) {
    this.apply {
        attributes.screenBrightness = value
        addFlags(SCREEN_BRIGHTNESS_CHANGED)
    }
}