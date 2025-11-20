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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.text.CustomTextField
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

private interface AuthScreenCallback{
    fun openMainScreen() {}
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
    navController: NavHostController = rememberNavController()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val callback = object: AuthScreenCallback{
        override fun openMainScreen() {
            navController.navigate(AppRouts.MAIN)
        }

        override fun openForgotScreen() {
            navController.navigate(AppRouts.FORGOT_PASSWORD)
        }

        override fun openRegistrationScreen() {
            navController.navigate(AppRouts.REGISTER_ACCOUNT)
        }
    }

    Content(
        email = email,
        password = password,
        onEmailChange = { email = it },
        callback = callback,
        onPasswordChange = { password = it }
    )
}

@Composable
private fun Content(
    email: String,
    password: String,
    callback: AuthScreenCallback,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.block)
            .padding(top = 121.dp, start = 20.dp, end = 20.dp, bottom = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadingAndUnderHeadingAuth()
        Spacer(modifier = Modifier.height(35.dp))
        AuthContent(
            email = email,
            password = password,
            openMainScreen = callback::openMainScreen,
            openForgotScreen = callback::openForgotScreen,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange
        )
        Spacer(modifier = Modifier.height(209.dp))
        ActionsTexts(
            text = R.string.new_user,
            textClick = R.string.create_account,
            onClick = callback::openRegistrationScreen
        )
    }
}

@Composable
fun HeadingAndUnderHeadingAuth() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.auth_hello_heading),
            color = Colors.text,
            style = MatuleTypography.headlineLarge
        )
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.auth_under_heading),
            color = Colors.subTextDark,
            style = MatuleTypography.titleMedium
        )
    }
}

@Composable
private fun AuthContent(
    email: String,
    password: String,
    openMainScreen: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    openForgotScreen: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

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

    Column(
        horizontalAlignment = Alignment.End
    ) {
        CustomTextField(
            query = email,
            onTextChange = onEmailChange,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textError = R.string.auth_error_email,
            placeholder = "example@gmail.com"
        )
        Spacer(modifier = Modifier.height(26.dp))
        CustomTextField(
            query = password,
            onTextChange = onPasswordChange,
            label = R.string.password,
            textError = R.string.auth_error_password,
            placeholder = "********",
            visualTransformation = transformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = { passwordVisible = !passwordVisible })
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.recovery_password),
            color = Colors.subTextDark,
            style = MatuleTypography.bodySmall,
            modifier = Modifier.clickable(onClick = openForgotScreen)
        )
        Spacer(modifier = Modifier.height(24.dp))
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.btn_sign_in,
            onClick = openMainScreen
        )
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

