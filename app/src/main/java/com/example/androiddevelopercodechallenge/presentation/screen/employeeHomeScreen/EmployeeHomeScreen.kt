package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.presentation.theme.AvatarCircle
import com.example.androiddevelopercodechallenge.presentation.theme.Body
import com.example.androiddevelopercodechallenge.presentation.theme.CardBackground
import com.example.androiddevelopercodechallenge.presentation.theme.Label
import com.example.androiddevelopercodechallenge.presentation.theme.SearchBarBG
import com.example.androiddevelopercodechallenge.presentation.theme.SearchIcon
import com.example.androiddevelopercodechallenge.presentation.theme.SearchPlaceholder
import com.example.androiddevelopercodechallenge.presentation.theme.Tryes
import com.example.androiddevelopercodechallenge.presentation.theme.Typography
import com.example.androiddevelopercodechallenge.presentation.util.ProfilePicture
import com.example.androiddevelopercodechallenge.presentation.util.ShimmerEffect
import com.example.androiddevelopercodechallenge.presentation.util.ShowDialog
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmployeeHomeScreen(
    modifier: Modifier = Modifier,
    state: EmployeeHomeUiState,
    onActions: (EmployeeHomeActions) -> Unit,
) {
    Log.d("MyTag","searchQuery: ${state.searchQuery}")
    Scaffold(
        floatingActionButton = {
            FloatingButtonComposable(
                isLoading = state.isLoading,
                onClick = { onActions(EmployeeHomeActions.NavigateToAddEmployee) }
            )
        }
    ) { _ ->
        Column(
            modifier = modifier
        ) {
            if (state.showDialog) {
                ShowDialog(
                    modifier = Modifier,
                    title = stringResource(R.string.delete_employee),
                    isProgressBar = state.dialogProgressBar,
                    description = {
                        if (state.dialogProgressBar) {
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
                                text = "${
                                    stringResource(R.string.are_you_sure_want_to_delete)
                                }: ${state.currentEmployee.name.first} ${state.currentEmployee.name.last}?"
                            )
                        }
                    },
                    confirmText = stringResource(R.string.delete),
                    confirmButton = {
                        onActions(EmployeeHomeActions.DeleteEmployeeConfirmed)
                    },
                    onDismissButton = {
                        onActions(EmployeeHomeActions.HideDialog)
                    }
                )
            }
            val employeeItems = state.employeePagingData.collectAsLazyPagingItems()

            Log.d("MyTag", "employeeItems: ${employeeItems.itemCount}")

            Log.d("MyTag", "employeeListSize: ${state.employeeList.size}")

            Log.d("MyTag", "filteredEmployeeListSize: ${state.filteredEmployeeList.size}")


            LaunchedEffect(Unit) {
                //converting employeeItems from state to flow
                snapshotFlow { employeeItems.itemSnapshotList.items }
                    .collectLatest { itemList ->
                        onActions(EmployeeHomeActions.UpdateEmployeeList(newEmployeeList = itemList))
                    }
            }


            if (state.isLoading) {
                ShimmerSearchView()
            } else {
                SearchView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    searchQuery = state.searchQuery,
                    onClear = { onActions(EmployeeHomeActions.ClearQuery) },
                    onQueryChange = { newQuery ->
                        onActions(EmployeeHomeActions.OnSearchQueryChanged(newQuery = newQuery))
                    }
                )
            }

            if (state.searchQuery.isBlank()) {
                //to remove scrolling effect in lazyColumn
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.Start,
                        contentPadding = PaddingValues(vertical = 20.dp),
                    ) {
                        items(count = employeeItems.itemCount) { index ->
                            val item = employeeItems[index]!!

                            EmployeeCard(
                                modifier = Modifier.fillMaxWidth(),
                                employee = item,
                                onClick = { employee ->
                                    onActions(EmployeeHomeActions.NavigateToEmployeeDetail(employee = employee))
                                },
                                onDeleteClick = { employee ->
                                    onActions(EmployeeHomeActions.DeleteEmployee(employee = employee))
                                }
                            )
                        }

                        when (employeeItems.loadState.append) {
                            is LoadState.Error -> Unit
                            LoadState.Loading -> {
                                item {
                                    LoadingItems()
                                }
                            }

                            is LoadState.NotLoading -> Unit
                        }

                        //when we first show it
                        when (employeeItems.loadState.refresh) {
                            is LoadState.Error -> Unit
                            LoadState.Loading -> {
                                item {
                                    repeat(8) {
                                        Column(modifier = Modifier) {
                                            ShimmerEmployeeCard()
                                            Spacer(modifier = Modifier.height(20.dp))
                                        }
                                    }
                                }
                            }

                            is LoadState.NotLoading -> onActions(
                                EmployeeHomeActions.UpdateLoader(
                                    isLoading = false
                                )
                            )
                        }
                    }
                }
            } else {
                //to remove scrolling effect in lazyColumn
                CompositionLocalProvider(LocalOverscrollConfiguration provides null){
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(vertical = 20.dp)
                    ) {
                        items(count = state.filteredEmployeeList.size) { index ->
                            val item = state.filteredEmployeeList[index]

                            EmployeeCard(
                                modifier = Modifier.fillMaxWidth(),
                                employee = item,
                                onClick = { employee ->
                                    onActions(EmployeeHomeActions.NavigateToEmployeeDetail(employee = employee))
                                },
                                onDeleteClick = { employee ->

                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun ShimmerEmployeeCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 10.dp, end = 10.dp)
                        .align(Alignment.TopEnd),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent
                    )
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 66.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    repeat(3) {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
        }

        // Shimmer profile picture
        ShimmerEffect(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .clip(CircleShape)
                .background(AvatarCircle)
        )
    }
}



@Composable
fun EmployeeCard(
    modifier: Modifier = Modifier,
    employee: Result,
    onClick: (Result) -> Unit,
    onDeleteClick: (Result) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .padding(top = 50.dp)
                .clip(RoundedCornerShape(size = 12.dp))
                .clickable(
                    onClick = {
                        onClick(employee)
                    }),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        onDeleteClick(employee)
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 10.dp, end = 10.dp)
                        .align(Alignment.TopEnd),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Red,
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = LocalContentColor.current,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 66.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = "${employee.name.first} ${employee.name.last}",
                        style = Typography.titleMedium
                    )

                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.street) + ": ${employee.location.street.name}, ${employee.location.street.number}",
                        style = Typography.bodyMedium
                    )

                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.id) + ": ${employee.id.value}",
                        style = Typography.labelSmall
                    )

                }

            }
        }

        ProfilePicture(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopCenter)
                .clip(CircleShape)
                .background(AvatarCircle),
            imageUrl = employee.picture.large
        )
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
    onClear: () -> Unit,
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
        trailingIcon = {
            IconButton(
                onClick = onClear,
                colors = IconButtonDefaults.iconButtonColors(contentColor = SearchIcon)
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.clear),
                    tint = LocalContentColor.current
                )
            }
        },
        textStyle = Typography.bodyMedium,
        maxLines = 1,
        value = searchQuery,
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
            focusedLeadingIconColor = SearchIcon,
            unfocusedLeadingIconColor = SearchIcon,
            disabledTrailingIconColor = SearchIcon,
            focusedTrailingIconColor = SearchIcon,


            ),
        placeholder = {
            Text(text = stringResource(R.string.search), color = LocalContentColor.current)
        },
    )
}

@Composable
fun FloatingButtonComposable(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = if (isLoading) Label else Tryes,
        contentColor = if (isLoading) Color.White else Color.White
    ) {
        if (isLoading) {
            ShimmerEffect(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_employee),
                tint = LocalContentColor.current
            )
        }
    }
}

@Preview
@Composable
fun ProfilePicturePreview() {
    ProfilePicture(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        imageUrl = "https://randomuser.me/api/portraits/women/55.jpg"
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
                .size(42.dp)
                .padding(8.dp),
            strokeWidth = 5.dp,
            color = Label.copy(alpha = 0.3f),
        )
    }
}
