package com.example.matule.ui.presentation.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.auth.viewmodel.AuthScreenContract
import com.example.matule.ui.presentation.feature.auth.viewmodel.AuthScreenViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.text.CustomTextField
import com.example.matule.ui.presentation.shared.text.PasswordTextField
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

private interface AuthScreenCallback{
    fun openMainScreen(email: String, password: String) {}
    fun openForgotScreen() {}
    fun openRegistrationScreen() {}
}


@Preview
@Composable
private fun Prev() {
    AuthScreen()
}

@Composable
fun AuthScreen(
    vm: AuthScreenViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {

    val state by vm.state.collectAsState()
    val authData by vm.authData.collectAsState()
    val effects = vm.effect.collectAsState(initial = null)

    val callback = object : AuthScreenCallback {
        override fun openMainScreen(email: String, password: String) {
            vm.handleEvent(AuthScreenContract.Event.Login(email, password))
        }

        override fun openForgotScreen() {
            navController.navigate(AppRouts.FORGOT_PASSWORD)
        }

        override fun openRegistrationScreen() {
            navController.navigate(AppRouts.REGISTER)
        }
    }

    LaunchedEffect(effects.value) {
        effects.value?.let { effect ->
            when (effect) {
                is AuthScreenContract.Effect.NavigateToMain -> {
                    navController.navigate(AppRouts.MAIN) {
                        popUpTo(AppRouts.AUTH) { inclusive = true }
                    }
                }

                is AuthScreenContract.Effect.NavigateToLogin -> {
                    navController.navigate(AppRouts.AUTH) {
                        popUpTo(AppRouts.MAIN) { inclusive = true }
                    }
                }

                is AuthScreenContract.Effect.ShowError -> {}
            }
        }
    }

    Content(
        authData = authData,
        onEmailChange = { vm.updateEmail(it) },
        callback = callback,
        onClearError = { vm.handleEvent(AuthScreenContract.Event.ClearError) },
        onPasswordChange = { vm.updatePassword(it) },
        state = state
    )

}

@Composable
private fun Content(
    state: AuthScreenContract.State,
    authData: AuthScreenViewModel.AuthData,
    callback: AuthScreenCallback,
    onClearError: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.block)
            .padding(top = 121.dp, start = 20.dp, end = 20.dp, bottom = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            HeadingAndUnderHeadingAuth(
                heading = R.string.auth_hello_heading,
                underHeading = R.string.auth_under_heading
            )
            Spacer(modifier = Modifier.height(35.dp))
            AuthContent(
                authData = authData,
                openMainScreen = callback::openMainScreen,
                openForgotScreen = callback::openForgotScreen,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onClearError = onClearError,
                state = state
            )
        }
        ActionsTexts(
            text = R.string.new_user,
            textClick = R.string.create_account,
            onClick = callback::openRegistrationScreen
        )
    }
}

@Composable
fun HeadingAndUnderHeadingAuth(
    heading: Int,
    underHeading: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(heading),
            color = Colors.text,
            style = MatuleTypography.headlineLarge
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(underHeading),
            color = Colors.subTextDark,
            style = MatuleTypography.titleMedium
        )
    }
}

@Composable
private fun AuthContent(
    state: AuthScreenContract.State,
    onClearError: () -> Unit,
    authData: AuthScreenViewModel.AuthData,
    openMainScreen: (String, String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    openForgotScreen: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val hasError = state is AuthScreenContract.State.Error

    var isPasswordError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }

    val icon =  if (passwordVisible) {
        R.drawable.ic_eye_open
    } else {
        R.drawable.ic_eye_close
    }

    val transformation = if (passwordVisible){
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
    LaunchedEffect(authData.email) {
        if (hasError) {
            onClearError()
        }
        isEmailError = false
    }

    LaunchedEffect(authData.password) {
        if (hasError) {
            onClearError()
        }
        isPasswordError = false
    }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        CustomTextField(
            query = authData.email,
            onTextChange = {
                onEmailChange(it)
                isEmailError = false
            },
            label = R.string.email,
            isError = isEmailError || hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textError = if (isEmailError) R.string.register_enter_email else R.string.empty_text,
            placeholder = "example@gmail.com"
        )
        Spacer(modifier = Modifier.height(26.dp))
        PasswordTextField(
            password = authData.password,
            onPasswordChange = {
                onPasswordChange(it)
                isPasswordError = false
            },
            icon = icon,
            isError = isPasswordError || hasError,
            onCLickEye = { passwordVisible = !passwordVisible},
            transformation = transformation,
            textError = if (isPasswordError) R.string.register_enter_password else R.string.empty_text
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.recovery_password),
            color = Colors.subTextDark,
            style = MatuleTypography.bodySmall,
            modifier = Modifier.clickable(onClick = openForgotScreen)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.btn_sign_in,
                onClick = {
                    isEmailError = authData.email.isBlank() || !authData.email.contains("@")
                    isPasswordError = authData.password.length < 6

                    if (!isEmailError && !isPasswordError) {
                        openMainScreen(authData.email, authData.password)
                    }
                }
            )
            if (hasError) {
                Text(
                    text = "Неправильный пароль или email!",
                    color = Colors.red,
                    style = MatuleTypography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ActionsTexts(
    text: Int,
    textClick: Int,
    onClick: () -> Unit
) {

    Row {
        Text(
            text = stringResource(text),
            color = Colors.hint,
            fontSize = 16.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
        Text(
            text = stringResource(textClick),
            color = Colors.text,
            fontSize = 16.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            modifier = Modifier.clickable(onClick = onClick)
        )
    }

}

