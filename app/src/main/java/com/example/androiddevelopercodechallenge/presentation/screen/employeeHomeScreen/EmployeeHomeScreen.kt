package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.presentation.theme.AvatarCircle
import com.example.androiddevelopercodechallenge.presentation.theme.Body
import com.example.androiddevelopercodechallenge.presentation.theme.CardBackground
import com.example.androiddevelopercodechallenge.presentation.theme.Label
import com.example.androiddevelopercodechallenge.presentation.theme.Roboto_Semi_bold
import com.example.androiddevelopercodechallenge.presentation.theme.SearchBarBG
import com.example.androiddevelopercodechallenge.presentation.theme.SearchIcon
import com.example.androiddevelopercodechallenge.presentation.theme.SearchPlaceholder
import com.example.androiddevelopercodechallenge.presentation.theme.Tryes
import com.example.androiddevelopercodechallenge.presentation.theme.Typography
import com.example.androiddevelopercodechallenge.presentation.util.PagingError
import com.example.androiddevelopercodechallenge.presentation.util.ProfilePicture
import com.example.androiddevelopercodechallenge.presentation.util.ShimmerEffect
import com.example.androiddevelopercodechallenge.presentation.util.ShowDialog


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmployeeHomeScreen(
    modifier: Modifier = Modifier,
    state: EmployeeHomeUiState,
    dialogState: EmployeeHomeDialogState,
    employeeHomeViewModel: EmployeeHomeViewModel = hiltViewModel<EmployeeHomeViewModel>(),
    onActions: (EmployeeHomeActions) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            if (state.isLoading) {
                ShimmerFloatingButton()
            } else {
                FloatingButtonComposable(
                    onClick = { onActions(EmployeeHomeActions.NavigateToAddEmployee) }
                )
            }
        }
    ) { _ ->
        Box(
            modifier = modifier
        ) {
            EmployeeContent(
                state = state,
                employeeHomeViewModel = employeeHomeViewModel,
                onActions = onActions
            )

            ShowDialog(
                modifier = Modifier,
                title = stringResource(R.string.delete_employee),
                isProgressBar = dialogState.dialogProgressBar,
                showDialog = dialogState.showDialog,
                description = {
                    if (dialogState.dialogProgressBar) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.Center),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        Text(
                            modifier = Modifier,
                            text = buildAnnotatedString {
                                append(stringResource(R.string.are_you_sure_want_to_delete))
                                append(": ")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = Roboto_Semi_bold,
                                        fontSize = 16.sp,
                                        color = Color.Red,
                                    )
                                ) {
                                    append("${state.currentEmployee.name.first} ${state.currentEmployee.name.last}")
                                }
                                append("?")
                            }
                        )
                    }
                },
                confirmText = stringResource(R.string.delete),
                confirmButton = {
                    onActions(EmployeeHomeActions.DeleteEmployeeConfirmed(employee = state.currentEmployee))
                },
                onDismissButton = {
                    onActions(EmployeeHomeActions.HideDialog)
                }
            )


        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmployeeContent(
    state: EmployeeHomeUiState,
    employeeHomeViewModel: EmployeeHomeViewModel,
    onActions: (EmployeeHomeActions) -> Unit,
) {
    Column(
        modifier = Modifier
    ) {
        val filteredEmployeePagingFlow = employeeHomeViewModel.filteredEmployeePagingFlow.collectAsLazyPagingItems()

        val loadState = filteredEmployeePagingFlow.loadState

        if (state.isLoading) {
            ShimmerSearchView()
            Spacer(modifier = Modifier.height(20.dp))
            repeat(8) {
                Column(modifier = Modifier) {
                    ShimmerEmployeeCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            SearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                searchQuery = state.searchQuery,
                enableButton = state.enableButtons,
                onQueryChange = { newQuery ->
                    onActions(EmployeeHomeActions.OnSearchQueryChanged(newQuery = newQuery))
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        when (loadState.refresh) {
            is LoadState.Error -> {
                onActions(EmployeeHomeActions.UpdateLoader(isLoading = false))
                PagingError(
                    id = R.string.refresh,
                    onPagingPerform = {
                        //refresh() creating new paging like fresh start page = 1(initial set)
                        filteredEmployeePagingFlow.refresh()
                    },
                )
            }

            LoadState.Loading -> {
                onActions(EmployeeHomeActions.UpdateLoader(isLoading = true))
            }

            is LoadState.NotLoading -> {
                onActions(EmployeeHomeActions.UpdateLoader(isLoading = false))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
        ) {
            if (state.searchQuery.isBlank()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    //id can be null
                    //key for better performance
                    items(
                        count = filteredEmployeePagingFlow.itemCount,
                        key = { index ->
                            filteredEmployeePagingFlow[index]?.email ?: "emptyEmail_$index"
                        }
                    ) { index ->
                        val item = filteredEmployeePagingFlow[index]
                        if (item != null) {
                            EmployeeCard(
                                modifier = Modifier.fillMaxWidth(),
                                employee = item,
                                onClick = { employee, checked ->
                                    onActions(
                                        EmployeeHomeActions.NavigateToEditEmployee(
                                            employee = employee,
                                            checked = checked
                                        )
                                    )
                                },
                                onDeleteClick = { employee ->
                                    onActions(
                                        EmployeeHomeActions.DeleteEmployee(
                                            employee = employee
                                        )
                                    )
                                },
                                onEditClick = { employee, checked ->
                                    onActions(
                                        EmployeeHomeActions.NavigateToEditEmployee(
                                            employee = employee,
                                            checked = checked
                                        )
                                    )
                                })
                        }
                    }

                    when (loadState.append) {
                        is LoadState.Error -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                //retry() resume from last page
                                PagingError(
                                    id = R.string.retry,
                                    onPagingPerform = { filteredEmployeePagingFlow.retry() }
                                )
                            }
                        }

                        LoadState.Loading -> {
                            onActions(
                                EmployeeHomeActions.UpdateLoader(
                                    isLoading = false
                                )
                            )
                            //GridItemSpan(maxLineSpan) placing it in single row to center
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Column {
                                    LoadingItems()
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }

                        }

                        is LoadState.NotLoading -> {
                        }
                    }
                }
            }
            else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(
                        items = state.filteredEmployeeResult,
                    ) { item ->
                        EmployeeCard(
                            modifier = Modifier.fillMaxWidth(),
                            employee = item,
                            onClick = { employee, checked ->
                                onActions(
                                    EmployeeHomeActions.NavigateToEditEmployee(
                                        employee = employee,
                                        checked = checked
                                    )
                                )
                            },
                            onDeleteClick = { employee ->
                                onActions(
                                    EmployeeHomeActions.DeleteEmployee(
                                        employee = employee
                                    )
                                )
                            },
                            onEditClick = { employee, checked ->
                                onActions(
                                    EmployeeHomeActions.NavigateToEditEmployee(
                                        employee = employee,
                                        checked = checked
                                    )
                                )
                            })
                    }
                }
            }

        }


    }
}

@Preview
@Composable
fun ShimmerEmployeeCard(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(count = 8) {
            Box(
                modifier = Modifier
            ) {
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Box {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ShimmerEffect(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth(0.6f)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            ShimmerEffect(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            ShimmerEffect(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))

                            // Row with icon buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(2) {
                                    ShimmerEffect(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }

                        // Large shimmer profile picture on top
                        ShimmerEffect(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(80.dp)
                                .align(Alignment.TopCenter)
                                .clip(CircleShape)
                                .background(AvatarCircle)
                                .border(2.dp, Color.White, shape = CircleShape)
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun EmployeeCard(
    modifier: Modifier = Modifier,
    employee: Result,
    onClick: ((Result, Boolean) -> Unit)? = null,
    onDeleteClick: (Result) -> Unit,
    onEditClick: (Result, Boolean) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(size = 12.dp))
                .clickable(
                    onClick = {
                        if (onClick != null) {
                            onClick(employee, false)
                        }
                    }),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
        ) {
            Box {
                Column(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePicture(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        imageUrl = employee.picture.thumbnail,
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "${employee.name.first} ${employee.name.last}",
                            style = Typography.titleMedium,
                            maxLines = 1,
                            textAlign = TextAlign.Center

                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.street) + ": ${employee.location.street.streetName}, ${employee.location.street.number}",
                            style = Typography.bodyMedium,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.id) + ": ${employee.id.value}",
                            style = Typography.labelSmall,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    onDeleteClick(employee)
                                },
                                modifier = Modifier
                                    .size(28.dp),
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color.Red,
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Icon(
                                    modifier = Modifier,
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = LocalContentColor.current,
                                )
                            }

                            IconButton(
                                onClick = {
                                    onEditClick(employee, true)
                                },
                                modifier = Modifier
                                    .size(28.dp),
                            ) {
                                Icon(
                                    modifier = Modifier,
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = stringResource(R.string.edt),
                                    tint = Color.Unspecified,
                                )
                            }
                        }
                    }
                }

                ProfilePicture(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(80.dp)
                        .align(Alignment.TopCenter)
                        .clip(CircleShape)
                        .background(AvatarCircle)
                        .border(2.dp, Color.White, shape = CircleShape),
                    imageUrl = employee.picture.large,
                    contentScale = ContentScale.Crop
                )
            }
        }

    }
}


@Composable
fun ShimmerSearchView() {
    ShimmerEffect(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(25.dp))
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchView(
    modifier: Modifier,
    searchQuery: String,
    enableButton: Boolean,
    onQueryChange: (String) -> Unit
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


    TextField(
        modifier = modifier
            .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(25.dp)),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search),
                tint = LocalContentColor.current
            )
        },
        textStyle = Typography.bodyMedium,
        maxLines = 1,
        value = searchQuery,
        enabled = enableButton,
        onValueChange = onQueryChange,
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = SearchBarBG,
            focusedTextColor = Body,
            unfocusedTextColor = SearchPlaceholder,
            unfocusedPlaceholderColor = SearchPlaceholder,
            disabledPlaceholderColor = SearchPlaceholder.copy(alpha = 0.3f),
            focusedLeadingIconColor = SearchIcon,
            unfocusedLeadingIconColor = SearchIcon,
            disabledLeadingIconColor = SearchIcon.copy(alpha = 0.3f),
            focusedTrailingIconColor = SearchIcon,
            unfocusedTrailingIconColor = SearchIcon,
            disabledTrailingIconColor = SearchIcon.copy(alpha = 0.3f),
            disabledIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(text = stringResource(R.string.search), color = LocalContentColor.current)
        },
    )
}

@Composable
fun ShimmerFloatingButton() {
    ShimmerEffect(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
    )
}

@Composable
fun FloatingButtonComposable(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick
        ,
        shape = CircleShape,
        containerColor = Tryes,
        contentColor = Color.White,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_employee),
            tint = LocalContentColor.current
        )
    }
}

@Preview
@Composable
fun ProfilePicturePreview() {
    ProfilePicture(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        imageUrl = "https://randomuser.me/api/portraits/women/55.jpg",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun LoadingItems() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(52.dp)
                .padding(8.dp),
            strokeWidth = 5.dp,
            color = Label.copy(alpha = 0.5f),
        )
    }
}
