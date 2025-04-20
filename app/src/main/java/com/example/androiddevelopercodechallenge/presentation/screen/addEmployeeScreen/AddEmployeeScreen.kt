package com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.Country
import com.example.androiddevelopercodechallenge.presentation.theme.Label
import com.example.androiddevelopercodechallenge.presentation.theme.Roboto_Bold
import com.example.androiddevelopercodechallenge.presentation.theme.SearchPlaceholder
import com.example.androiddevelopercodechallenge.presentation.theme.Tryes
import com.example.androiddevelopercodechallenge.presentation.theme.Typography
import com.example.androiddevelopercodechallenge.presentation.util.Constants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddEmployeeScreen(
    modifier: Modifier,
    state: AddEmployeeUiState,
    onActions: (AddEmployeeActions) -> Unit,
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
                        onActions(AddEmployeeActions.AddEmployee)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Tryes,
                        contentColor = Color.White,
                        disabledContainerColor = SearchPlaceholder,
                        disabledContentColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = stringResource(R.string.add_employee),
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
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.personal_information),
                    style = Typography.titleLarge
                )

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
                        onActions(AddEmployeeActions.UpdateFirstName(firstName = newFirstName))
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                AddTextFieldForm(
                    textInput = R.string.last_name,
                    inputValue = state.employee.name.last,
                    onInputChange = { newLastName ->
                        onActions(AddEmployeeActions.UpdateLastName(lastName = newLastName))
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                AddTextFieldForm(
                    textInput = R.string.email,
                    inputValue = state.employee.email,
                    onInputChange = { newEmail ->
                        onActions(AddEmployeeActions.UpdateEmail(email = newEmail))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = stringResource(R.string.email)
                        )
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextLabel(textInput = R.string.phone_number)

                PhoneInputWithCountryCode(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCountry = state.selectedCountry,
                    isExpanded = state.isCountryExpanded,
                    onExpand = {
                        onActions(AddEmployeeActions.OnCountryExpand)
                    },
                    onCountrySelect = { selectedCountry ->
                        onActions(AddEmployeeActions.UpdateSelectedCountry(country = selectedCountry))
                    },
                    phoneNUmberValue = state.employee.phone,
                    onPhoneNumberChange = { newPhoneNumber ->
                        onActions(AddEmployeeActions.UpdatePhoneNumber(phoneNumber = newPhoneNumber))
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextLabel(textInput = R.string.gender)

                GenderDropDown(
                    modifier = Modifier,
                    isGenderExpanded = state.isGenderExpanded,
                    selectedGender = state.employee.gender,
                    onExpand = {
                        onActions(AddEmployeeActions.OnGenderExpand)
                    },
                    onGenderSelect = { newGender ->
                        onActions(AddEmployeeActions.UpdateSelectedGender(gender = newGender))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropDown(
    modifier: Modifier,
    isGenderExpanded: Boolean,
    selectedGender: String,
    onExpand: () -> Unit,
    onGenderSelect: (String) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isGenderExpanded,
        onExpandedChange = {
            onExpand()
        }
    ) {
        GenderSelectedDropDown(
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable,
                enabled = true
            ),
            selectedGender = selectedGender,
            isExpanded = isGenderExpanded,
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth(),
            matchTextFieldWidth = false,
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White,
            expanded = isGenderExpanded,
            onDismissRequest = {
                onExpand()
            },
        ) {
            GenderItemList(
                selectedGender = selectedGender,
                onGenderSelect = onGenderSelect,
                onExpand = onExpand
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderItemList(
    genderList: List<String> = Constants.gender,
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    onExpand: () -> Unit,
) {
    genderList.forEach { gender ->
        DropdownMenuItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            text = {
                GenderItem(
                    gender = gender
                )
            },
            onClick = {
                onGenderSelect(gender)
                onExpand()
            },
            trailingIcon = {
                if (gender == selectedGender) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            },
        )
    }
}

@Composable
fun GenderItem(
    gender: String,
) {
    Text(
        modifier = Modifier,
        text = gender,
        style = Typography.bodyLarge,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectedDropDown(
    modifier: Modifier = Modifier,
    selectedGender: String,
    isExpanded: Boolean,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE0E0E0)
        ),
        color = Color.White,
        contentColor = Color.Unspecified,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = selectedGender,
                style = Typography.bodyLarge,
            )

            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
        }
    }
}

@Composable
fun PhoneInputWithCountryCode(
    modifier: Modifier,
    selectedCountry: Country,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onCountrySelect: (Country) -> Unit,
    phoneNUmberValue: String,
    onPhoneNumberChange: (String) -> Unit,
) {
    Row(
        //IntrinsicSize.Max height will be the max between CountryDropDown and AddTextFieldForm
        //to have the same height for both
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountryDropDown(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            selectedCountry = selectedCountry,
            isExpanded = isExpanded,
            onExpand = onExpand,
            onCountrySelect = onCountrySelect,
        )

        AddTextFieldForm(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            textInput = R.string.phone_number,
            inputValue = phoneNUmberValue,
            onInputChange = onPhoneNumberChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = stringResource(R.string.phone_number)
                )
            },
            isLastField = true,
            showText = false,
            isKeyboardNumber = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropDown(
    modifier: Modifier,
    isExpanded: Boolean,
    selectedCountry: Country,
    onExpand: () -> Unit,
    onCountrySelect: (Country) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = {
            onExpand()
        }
    ) {
        CountrySelectedDropDown(
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable,
                enabled = true
            ),
            selectedCountry = selectedCountry,
            isExpanded = isExpanded,
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth(),
            matchTextFieldWidth = false,
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White,
            expanded = isExpanded,
            onDismissRequest = {
                onExpand()
            },
        ) {
            CountryItemList(
                selectedCountry = selectedCountry,
                onCountrySelect = onCountrySelect,
                onExpand = onExpand
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryItemList(
    countryList: Map<String, Country> = Constants.countryList,
    selectedCountry: Country,
    onCountrySelect: (Country) -> Unit,
    onExpand: () -> Unit,
) {
    countryList.entries.forEach { (countryName, country) ->
        DropdownMenuItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
            text = {
                CountryItem(
                    currentCountry = country,
                    countryName = countryName,
                )
            },
            onClick = {
                onCountrySelect(country)
                onExpand()
            },
            trailingIcon = {
                if (country == selectedCountry) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            },
        )
    }
}

@Composable
fun CountryItem(
    currentCountry: Country,
    countryName: String,
) {
    Row(
        modifier = Modifier
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = currentCountry.countryFlag),
            contentDescription = stringResource(id = R.string.country),
            tint = Color.Unspecified
        )

        Text(
            modifier = Modifier,
            text = countryName + " " + currentCountry.extension,
            style = Typography.bodyLarge,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectedDropDown(
    modifier: Modifier = Modifier,
    selectedCountry: Country,
    isExpanded: Boolean,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE0E0E0)
        ),
        color = Color.White,
        contentColor = Color.Unspecified,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = selectedCountry.countryFlag),
                contentDescription = stringResource(id = R.string.country),
                tint = LocalContentColor.current
            )

            Text(
                modifier = Modifier,
                text = stringResource(R.string.id),
                style = Typography.bodyLarge,
            )

            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTextFieldForm(
    modifier: Modifier = Modifier, @StringRes textInput: Int, inputValue: String,
    placeHolderText: String = stringResource(R.string.typing),
    onInputChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    isLastField: Boolean = false,
    showText: Boolean = true,
    isKeyboardNumber: Boolean = false,
) {
    //keyboard controller to show or hide keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    //current focus manager if focused or not
    val focusManager = LocalFocusManager.current

    //check if keyboard is open or closed
    val isImeVisible = WindowInsets.isImeVisible

    //when keyboard closed clear focus (show unfocusedContainerColor)
    if (!isImeVisible) {
        focusManager.clearFocus()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if (showText) {
            TextLabel(
                textInput = textInput
            )
        }

        //outlined for borders
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = inputValue,
            onValueChange = { newInput ->
                onInputChange(newInput)
            },
            singleLine = true,
            textStyle = Typography.titleMedium.copy(color = Label),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = placeHolderText,
                    style = Typography.bodyMedium.copy(color = SearchPlaceholder)
                )
            },
            leadingIcon = leadingIcon,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,

                unfocusedIndicatorColor = Color(0xFFE0E0E0),
                focusedIndicatorColor = Color(0xFF0D47A1),

                unfocusedPlaceholderColor = Color(0xFF9E9E9E),
                unfocusedTextColor = Color(0xFF212121),
                focusedTextColor = Color(0xFF212121)
            ),
            //since Next is mostly true use it first in the condition
            keyboardOptions = KeyboardOptions(
                keyboardType = if (!isKeyboardNumber) KeyboardType.Text else KeyboardType.Phone,
                imeAction =
                    if (!isLastField) ImeAction.Next else ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    //move down
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onDone = {
                    //hide keyboard to clear focus
                    keyboardController?.hide()
                }),
        )
    }
}

@Composable
fun TextLabel(
    textInput: Int,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = buildAnnotatedString {
            append(stringResource(textInput))
            withStyle(
                SpanStyle(
                    color = Color.Red, fontSize = 18.sp,
                )
            ) {
                append("*")
            }
        },
        style = Typography.bodyLarge
    )

    Spacer(modifier = Modifier.height(8.dp))
}