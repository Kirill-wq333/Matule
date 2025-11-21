package com.example.matule.ui.presentation.shared.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography


@Composable
fun CustomTextField(
    query: String,
    textError: Int,
    label: Int,
    onTextChange: (String) -> Unit,
    isError: Boolean = false,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon:@Composable () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(label),
            color = Colors.text,
            style = MatuleTypography.bodyLarge
        )
        TextFieldWithTrailingIcon(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onTextChange = onTextChange,
            isError = isError,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            errorText = {
                Text(
                    text = stringResource(textError),
                    color = Colors.red,
                    style = MatuleTypography.bodySmall
                )
            },
            trailingIcon = trailingIcon,
        )
    }
}

@Composable
fun TextFieldWithTrailingIcon(
    modifier: Modifier = Modifier,
    query: String,
    onTextChange: (String) -> Unit,
    isError: Boolean = false,
    placeholder: String,
    errorText: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TextField(
            modifier = modifier
                .clip(RoundedCornerShape(14.dp)),
            value = query,
            onValueChange = { onTextChange(it) },
            maxLines = 1,
            textStyle = MatuleTypography.bodyMedium,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                cursorColor = Colors.text,
                focusedContainerColor = Colors.background,
                errorContainerColor = Colors.background,
                disabledContainerColor = Colors.background,
                unfocusedContainerColor = Colors.background,
                errorTextColor = Colors.red,
                disabledTextColor = Colors.text,
                focusedTextColor = Colors.text,
                unfocusedTextColor = Colors.hint,
                unfocusedTrailingIconColor = Colors.hint,
                focusedTrailingIconColor = Colors.text,
                disabledTrailingIconColor = Colors.hint,
                disabledLeadingIconColor = Colors.hint,
                focusedLeadingIconColor = Colors.hint,
                unfocusedLeadingIconColor = Colors.hint,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Colors.hint,
                    style = MatuleTypography.bodyMedium
                )
            },
            isError = isError,
            trailingIcon = {
                trailingIcon()
            },
        )
        if (isError) {
            errorText()
        }
    }
}

@Composable
fun TextFieldWithLeadingAndTrailingIcons(
    modifier: Modifier = Modifier,
    query: String,
    onTextChange: (String) -> Unit,
    isError: Boolean = false,
    placeholder: String,
    trailingIcon: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TextField(
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(14.dp),
                    clip = false
                )
                .clip(RoundedCornerShape(14.dp)),
            value = query,
            onValueChange = { onTextChange(it) },
            maxLines = 1,
            textStyle = MatuleTypography.bodyMedium,
            colors = TextFieldDefaults.colors(
                cursorColor = Colors.text,
                focusedContainerColor = Colors.block,
                errorContainerColor = Colors.block,
                disabledContainerColor = Colors.block,
                unfocusedContainerColor = Colors.block,
                errorTextColor = Colors.red,
                disabledTextColor = Colors.text,
                focusedTextColor = Colors.text,
                unfocusedTextColor = Colors.hint,
                unfocusedTrailingIconColor = Colors.hint,
                focusedTrailingIconColor = Colors.text,
                disabledTrailingIconColor = Colors.hint,
                disabledLeadingIconColor = Colors.hint,
                focusedLeadingIconColor = Colors.hint,
                unfocusedLeadingIconColor = Colors.hint,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Colors.hint,
                    style = MatuleTypography.bodyMedium
                )
            },
            isError = isError,
            trailingIcon = {
                trailingIcon()
            },
            leadingIcon = {
                leadingIcon()
            }
        )
    }
}