@file:Suppress("UNCHECKED_CAST")

package com.example.androiddevelopercodechallenge.presentation.screen.editUserScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditUiState
import com.example.androiddevelopercodechallenge.presentation.theme.Typography
import com.example.androiddevelopercodechallenge.presentation.util.AddOrEditUserScreen
import com.example.androiddevelopercodechallenge.presentation.util.SwitchButton

@Composable
fun EditUserScreen(
    modifier: Modifier,
    state: AddOrEditUiState.EditUserUiState,
    onActions: (AddOrEditActions.EditUserActions) -> Unit,
) {
    AddOrEditUserScreen(
        modifier = modifier,
        state = state,
        onActions = onActions as (AddOrEditActions) -> Unit,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.edit_information),
                    style = Typography.titleLarge
                )

                SwitchButton(
                    modifier = Modifier,
                    checked = state.readOnly,
                    onSwitch = { checked->
                        onActions(AddOrEditActions.EditUserActions.OnCheck(checked = checked))
                    },
                )
            }
        },
        checked = state.readOnly
    )
}