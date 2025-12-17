package com.example.matule.ui.presentation.feature.register.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.auth.ui.ActionsTexts
import com.example.matule.ui.presentation.feature.auth.ui.TitleAndSubTitleAuth
import com.example.matule.ui.presentation.feature.register.viewmodel.RegisterContract
import com.example.matule.ui.presentation.feature.register.viewmodel.RegisterViewModel
import com.example.matule.ui.presentation.shared.buttons.CheckboxPrivacyPolice
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.text.CustomTextField
import com.example.matule.ui.presentation.shared.text.PasswordTextField
import com.example.matule.ui.presentation.theme.Colors

@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    navController: NavHostController
) {

    Content(
        event = vm::handleEvent,
        navController = navController
    )
}

@Composable
private fun Content(
    event: (RegisterContract.Event) -> Unit,
    navController: NavHostController
) {

    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }

    var privacyPolice by remember { mutableStateOf(false) }

    var isPasswordError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.block)
            .padding(start = 20.dp, end = 20.dp, top = 78.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            TitleAndSubTitleAuth(
                heading = R.string.register_account,
                underHeading = R.string.auth_under_heading
            )
            Spacer(modifier = Modifier.height(30.dp))
            RegisterContent(
                isEmailError = isEmailError,
                isPasswordError = isPasswordError,
                email = email,
                firstName = firstName,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onFirstNameChange = { firstName = it },
                privacyPolice = privacyPolice,
                onCheckBox = { privacyPolice = !privacyPolice },
                openAuthScreen = {
                    isEmailError = email.isBlank() || !email.contains("@")
                    isPasswordError = password.length < 6

                    if (!isEmailError && !isPasswordError && privacyPolice) {
                        event(
                            RegisterContract.Event.Register(
                                email,
                                password,
                                firstName,
                            )
                        )
                        navController.navigate(AppRouts.AUTH)
                    }
                }
            )
        }
        ActionsTexts(
            text = R.string.already_have_account,
            textClick = R.string.btn_sign_in,
            onClick = { navController.navigate(AppRouts.AUTH) }
        )
    }
}

@Composable
fun RegisterContent(
    isEmailError: Boolean,
    isPasswordError: Boolean,
    email: String,
    firstName: String,
    password: String,
    onEmailChange:  (String) -> Unit,
    onPasswordChange:  (String) -> Unit,
    privacyPolice: Boolean,
    onCheckBox: () -> Unit,
    onFirstNameChange:  (String) -> Unit,
    openAuthScreen: () -> Unit
) {

    var passwordVisible by remember { mutableStateOf(false) }


    val icon = if (passwordVisible) {
        R.drawable.ic_eye_open
    } else {
        R.drawable.ic_eye_close
    }

    val transformation = if (passwordVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                query = firstName,
                onTextChange = onFirstNameChange,
                label = R.string.name,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                textError = R.string.register_enter_name,
                placeholder = "Иван"
            )

            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                query = email,
                onTextChange = onEmailChange,
                label = R.string.email,
                isError = isEmailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                textError = R.string.register_enter_email,
                placeholder = "example@gmail.com"
            )

            PasswordTextField(
                password = password,
                onPasswordChange = onPasswordChange,
                icon = icon,
                isError = isPasswordError,
                onCLickEye = { passwordVisible = !passwordVisible },
                transformation = transformation,
                textError = R.string.register_enter_password
            )
            Row(
                modifier = Modifier
                    .clickable(onClick = onCheckBox)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CheckboxPrivacyPolice(
                    isSelected = privacyPolice,
                )
                Text(
                    text = stringResource(R.string.checkbox_agree),
                    color = Colors.hint,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )
            }
        }
        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = R.string.btn_sign_up,
            onClick = openAuthScreen
        )
    }
}