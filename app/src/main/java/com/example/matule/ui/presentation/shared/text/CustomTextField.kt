@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.matule.ui.presentation.shared.text

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import java.util.Locale


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    query: String,
    textError: Int? = null,
    label: Int,
    onTextChange: (String) -> Unit,
    isError: Boolean = false,
    placeholder: String = "",
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon:@Composable () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = modifier,
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
            readOnly = readOnly,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            errorText = {
                Text(
                    text = stringResource(textError!!),
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
    readOnly: Boolean = false,
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
            singleLine = true,
            textStyle = MatuleTypography.bodyMedium,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
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
                errorTrailingIconColor = Colors.hint,
                focusedTrailingIconColor = Colors.text,
                disabledTrailingIconColor = Colors.hint,
                disabledLeadingIconColor = Colors.hint,
                focusedLeadingIconColor = Colors.hint,
                unfocusedLeadingIconColor = Colors.hint,
                errorLeadingIconColor = Colors.hint,
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
    query: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    searchScreen: Boolean,
    onSearch: (() -> Unit)? = null,
    leadingIcon: @Composable () -> Unit = {}
) {
    val width by animateFloatAsState(
        targetValue = if (searchScreen) 1f else 0.8f,
        animationSpec = tween(700)
    )

    var isListening by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var soundLevel by remember { mutableFloatStateOf(0f) }

    val microphoneColor by animateColorAsState(
        targetValue = if (isListening) Colors.accent else Colors.subTextDark,
        animationSpec = tween(300)
    )

    val currentScale by animateFloatAsState(
        targetValue = if (isListening) 1f + (soundLevel / 15f).coerceAtMost(0.3f) else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f
        )
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val recordAudioPermissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )

    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    val speechRecognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    val speechRecognitionListener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
            }

            override fun onBeginningOfSpeech() {
                isListening = true
            }

            override fun onRmsChanged(rmsdB: Float) {
                soundLevel = (rmsdB + 20f).coerceIn(0f, 10f)
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                isListening = false
                soundLevel = 0f
            }

            override fun onError(error: Int) {
                isListening = false

            }

            override fun onResults(results: Bundle?) {
                isListening = false
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { recognizedText ->
                    onTextChange(recognizedText)
                    onSearch?.invoke()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    LaunchedEffect(speechRecognizer) {
        speechRecognizer.setRecognitionListener(speechRecognitionListener)
    }

    DisposableEffect(speechRecognizer) {
        onDispose {
            speechRecognizer.destroy()
        }
    }

    fun startVoiceRecognition() {
        if (!recordAudioPermissionState.status.isGranted) {
            recordAudioPermissionState.launchPermissionRequest()
        } else {
            try {
                speechRecognizer.startListening(speechRecognizerIntent)
            } catch (e: SecurityException) {
                Log.e("SpeechRecognition", "SecurityException: ${e.message}")
            } catch (e: Exception) {
                Log.e("SpeechRecognition", "Exception: ${e.message}")
            }
        }
    }

    fun stopVoiceRecognition() {
        try {
            speechRecognizer.stopListening()
            isListening = false
        } catch (e: Exception) {
            Log.e("SpeechRecognition", "Stop error: ${e.message}")
        }
    }

    LaunchedEffect(isListening) {
        if (isListening) {
            while (isListening) {
                delay(100)
                if (soundLevel < 0.1f) {
                    delay(200)
                    if (soundLevel < 0.1f && isListening) {
                        soundLevel = 0f
                    }
                }
            }
        }
    }


    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(width)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(14.dp),
                    clip = false
                )
                .clip(RoundedCornerShape(14.dp)),
            value = query,
            onValueChange = { onTextChange(it) },
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch?.invoke()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            singleLine = true,
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
            trailingIcon = {
                AnimatedVisibility(
                    visible = width == 1f
                ) {
                    Row(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(end = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VerticalDivider(
                            thickness = 1.5.dp,
                            color = Colors.subTextDark
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .graphicsLayer {
                                    val combinedScale = if (isListening) {
                                        currentScale * pulseScale
                                    } else {
                                        currentScale
                                    }
                                    scaleX = combinedScale
                                    scaleY = combinedScale
                                }
                        ) {

                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_voice),
                                contentDescription = null,
                                tint = microphoneColor,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        if (isListening) {
                                            stopVoiceRecognition()
                                        } else {
                                            startVoiceRecognition()
                                        }
                                    }

                            )
                        }
                    }
                }
            },
            leadingIcon = {
                leadingIcon()
            }
        )
    }
}

