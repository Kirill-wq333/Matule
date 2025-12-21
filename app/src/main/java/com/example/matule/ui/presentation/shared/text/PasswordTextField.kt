package com.example.matule.ui.presentation.shared.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.example.matule.R

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    transformation: VisualTransformation,
    isError: Boolean = false,
    textError: Int?,
    icon: Int,
    onCLickEye: () -> Unit
) {

    CustomTextField(
        modifier = Modifier.fillMaxWidth(),
        query = password,
        onTextChange = onPasswordChange,
        label = R.string.password,
        textError = textError,
        isError = isError,
        placeholder = "••••••",
        visualTransformation = transformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onCLickEye)
            )
        }
    )
}

