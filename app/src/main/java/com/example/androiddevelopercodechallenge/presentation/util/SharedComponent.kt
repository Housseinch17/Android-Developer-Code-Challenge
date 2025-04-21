package com.example.androiddevelopercodechallenge.presentation.util

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditUiState
import com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen.AddTextFieldForm
import com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen.GenderDropDown
import com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen.PhoneInputWithCountryCode
import com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen.TextLabel
import com.example.androiddevelopercodechallenge.presentation.theme.Label
import com.example.androiddevelopercodechallenge.presentation.theme.Roboto_Bold
import com.example.androiddevelopercodechallenge.presentation.theme.SearchPlaceholder
import com.example.androiddevelopercodechallenge.presentation.theme.Tryes
import com.example.androiddevelopercodechallenge.presentation.theme.Typography


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddOrEditEmployeeScreen(
    modifier: Modifier,
    state: AddOrEditUiState,
    onActions: (AddOrEditActions) -> Unit,
    content: @Composable () -> Unit,
    checked: Boolean
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF2F3F5),
        contentColor = Color.Red,
        bottomBar = {
            Column(
                modifier = Modifier.imePadding(),
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    //imePadding() to show button above keyboard when it's open
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onActions(AddOrEditActions.AddOrSaveEmployee)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Tryes,
                        contentColor = Color.White,
                        disabledContainerColor = SearchPlaceholder,
                        disabledContentColor = Color.White.copy(alpha = 0.3f)
                    ),
                    enabled = checked
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = if (state is AddOrEditUiState.AddEmployeeUiState) stringResource(R.string.add_employee)
                        else {
                            stringResource(R.string.save)
                        },
                        //take color given by parent (contentColor = Background)
                        style = Typography.bodyLarge.copy(
                            fontFamily = Roboto_Bold,
                            color = LocalContentColor.current
                        )
                    )
                }

            }
        }
    ) { innerPadding ->
        //to remove scrolling effect in lazyColumn
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            Column(
                modifier = Modifier
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                content()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.let_us_know___basic_info),
                    style = Typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier,
                    text = "*" + stringResource(R.string.required_fields),
                    style = Typography.bodyMedium.copy(color = Color.Red)
                )

                Spacer(modifier = Modifier.height(20.dp))


                AddTextFieldForm(
                    textInput = R.string.first_name,
                    inputValue = state.employee.name.first,
                    onInputChange = { newFirstName ->
                        onActions(AddOrEditActions.UpdateFirstName(firstName = newFirstName))
                    },
                    checked = checked,
                )

                Spacer(modifier = Modifier.height(20.dp))

                AddTextFieldForm(
                    textInput = R.string.last_name,
                    inputValue = state.employee.name.last,
                    onInputChange = { newLastName ->
                        onActions(AddOrEditActions.UpdateLastName(lastName = newLastName))
                    },
                    checked = checked,
                )

                Spacer(modifier = Modifier.height(20.dp))

                AddTextFieldForm(
                    textInput = R.string.email,
                    inputValue = state.employee.email,
                    onInputChange = { newEmail ->
                        onActions(AddOrEditActions.UpdateEmail(email = newEmail))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = stringResource(R.string.email)
                        )
                    },
                    checked = checked,
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextLabel(textInput = R.string.phone_number)

                PhoneInputWithCountryCode(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCountry = state.selectedCountry,
                    isExpanded = state.isCountryExpanded,
                    onExpand = {
                        onActions(AddOrEditActions.OnCountryExpand)
                    },
                    onCountrySelect = { selectedCountry ->
                        onActions(AddOrEditActions.UpdateSelectedCountry(country = selectedCountry))
                    },
                    phoneNUmberValue = state.employee.phone,
                    onPhoneNumberChange = { newPhoneNumber ->
                        onActions(AddOrEditActions.UpdatePhoneNumber(phoneNumber = newPhoneNumber))
                    },
                    checked = checked,
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextLabel(textInput = R.string.gender)

                GenderDropDown(
                    modifier = Modifier,
                    isGenderExpanded = state.isGenderExpanded,
                    selectedGender = state.employee.gender,
                    onExpand = {
                        onActions(AddOrEditActions.OnGenderExpand)
                    },
                    onGenderSelect = { newGender ->
                        onActions(AddOrEditActions.UpdateSelectedGender(gender = newGender))
                    },
                    checked = checked,
                )
            }
        }
    }
}

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onSwitch: (Boolean)-> Unit,
){
    androidx.compose.material3.Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            onSwitch(it)
        },
        colors = SwitchDefaults.colors(checkedTrackColor = Tryes),
        thumbContent = if (checked) {
            {
                Icon(
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = LocalContentColor.current
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun PagingError(
    @StringRes id: Int,
    onPagingPerform: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(R.string.no_internet_connection),
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier,
            onClick = {
                onPagingPerform()
            }
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id)
            )
        }
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
) {
    val shimmerColors = listOf(
        color.copy(alpha = 0.3f),  // Light grey
        color.copy(alpha = 0.5f),  // Slightly more opaque
        color.copy(alpha = 0.7f),  // Darker grey
        color.copy(alpha = 0.5f),
        color.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentScale: ContentScale,
) {
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl).crossfade(true)
                .placeholder(R.drawable.loading)
                .error(R.drawable.connectionerror)
                .build(),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

@Composable
fun ShowDialog(
    modifier: Modifier,
    title: String,
    isProgressBar: Boolean,
    description: @Composable () -> Unit,
    confirmText: String,
    confirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
    AlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
        onDismissRequest = {},
        confirmButton = {
            Button(
                enabled = !isProgressBar,
                onClick = confirmButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text(
                    text = confirmText, color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            Button(
                enabled = !isProgressBar,
                onClick = onDismissButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Label
                )
            ) {
                Text(
                    stringResource(R.string.dismiss),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },

        title = {
            Text(
                text = title,
                style = Typography.titleMedium
            )
        },
        text = {
            description()
        })
}

fun phoneNumberVisualTransformation(extension: String): VisualTransformation {
    return VisualTransformation { text ->
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(extension)
            }
            append(" ")
            append(text.text)
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return extension.length + 1 + offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return (offset - extension.length - 1).coerceAtLeast(0)
            }
        }

        TransformedText(annotatedString, offsetMapping)
    }
}

