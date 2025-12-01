package com.example.matule.ui.presentation.feature.register.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.auth.ui.ActionsTexts
import com.example.matule.ui.presentation.feature.auth.ui.HeadingAndUnderHeadingAuth
import com.example.matule.ui.presentation.feature.register.viewmodel.RegisterContract
import com.example.matule.ui.presentation.feature.register.viewmodel.RegisterViewModel
import com.example.matule.ui.presentation.shared.buttons.CheckboxPrivacyPolice
import com.example.matule.ui.presentation.shared.buttons.CustomButton
import com.example.matule.ui.presentation.shared.text.CustomTextField
import com.example.matule.ui.presentation.shared.text.PasswordTextField
import com.example.matule.ui.presentation.theme.Colors
import kotlin.math.absoluteValue

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
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("7") }

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
            HeadingAndUnderHeadingAuth(
                heading = R.string.register_account,
                underHeading = R.string.auth_under_heading
            )
            Spacer(modifier = Modifier.height(30.dp))
            RegisterContent(
                isEmailError = isEmailError,
                isPasswordError = isPasswordError,
                phone = phone,
                email = email,
                firstName = firstName,
                lastName = lastName,
                password = password,
                onPhoneChange = { phone = it },
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLastNameChange = { lastName = it },
                onFirstNameChange = { firstName = it },
                privacyPolice = privacyPolice,
                onCheckBox = { privacyPolice = !privacyPolice },
                openAuthScreen = {
                    isEmailError = email.isBlank() || !email.contains("@")
                    isPasswordError = password.length < 6

                    if (!isEmailError && !isPasswordError && !privacyPolice) {
                        event(
                            RegisterContract.Event.Register(
                                email,
                                password,
                                firstName,
                                lastName,
                                phone
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
    phone: String,
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    onPhoneChange: (String) -> Unit,
    onEmailChange:  (String) -> Unit,
    onPasswordChange:  (String) -> Unit,
    onLastNameChange:  (String) -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomTextField(
                    modifier = Modifier.weight(0.5f),
                    query = firstName,
                    onTextChange = onFirstNameChange,
                    label = R.string.name,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    textError = R.string.register_enter_name,
                    placeholder = "Иван"
                )
                CustomTextField(
                    modifier = Modifier.weight(0.5f),
                    query = lastName,
                    onTextChange = onLastNameChange,
                    label = R.string.last_name,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    textError = null,
                    placeholder = "Иванов"
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomTextField(
                    modifier = Modifier.weight(0.5f),
                    query = email,
                    onTextChange = onEmailChange,
                    label = R.string.email,
                    isError = isEmailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textError = R.string.register_enter_email,
                    placeholder = "example@gmail.com"
                )
                CustomTextField(
                    modifier = Modifier.weight(0.5f),
                    query = phone,
                    onTextChange = {
                        onPhoneChange(it)
                    },
                    label = R.string.phone,
                    visualTransformation = rememberMaskVisualTransformation("+#(###)###-##-##"),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    textError = null,
                    placeholder = "+7 (999) 999-00-00"
                )
            }
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CheckboxPrivacyPolice(
                    isSelected = privacyPolice,
                    onClick = onCheckBox
                )
                Text(
                    text = stringResource(R.string.checkbox_agree),
                    color = Colors.hint,
                    fontSize = 16.sp,
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

private class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (mask.indices.filter { mask[it] != '#' }
                    .contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }; out += char; maskIndex++
        }

        return TransformedText(
            text = AnnotatedString(out),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val offsetValue = offset.absoluteValue
                    if (offsetValue == 0) return 0
                    var numberOfHashtags = 0
                    val masked = mask
                        .takeWhile {
                            if (it == '#') numberOfHashtags++
                            numberOfHashtags < offsetValue
                        }
                    return (masked.length + 1)
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return mask
                        .take(offset.absoluteValue)
                        .count { it == '#' }
                }
            }
        )

    }

}

@Composable
fun rememberMaskVisualTransformation(mask: String): VisualTransformation =
    remember(mask) { MaskVisualTransformation(mask) }