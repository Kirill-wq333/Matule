package com.example.matule.ui.presentation.shared.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    val placeholder = remember(password) {
        if (password.isEmpty()) {
            "••••••"
        } else {
            buildString {
                repeat(password.length.coerceAtLeast(1)) {
                    append('•')
                }
            }
        }
    }

    CustomTextField(
        modifier = Modifier.fillMaxWidth(),
        query = password,
        onTextChange = onPasswordChange,
        label = R.string.password,
        textError = textError,
        isError = isError,
        placeholder = placeholder,
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

