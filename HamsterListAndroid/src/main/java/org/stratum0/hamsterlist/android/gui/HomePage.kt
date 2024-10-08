package org.stratum0.hamsterlist.android.gui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import org.stratum0.hamsterlist.viewmodel.HomeUiState

@Composable
fun HomePage(
    uiState: HomeUiState,
    onLoadHamsterList: (username: String, hamsterListName: String, serverHostName: String) -> Unit,
) {
    var listId by rememberSaveable {
        mutableStateOf(uiState.currentListId)
    }
    var serverHostName by rememberSaveable(uiState.serverHostName) {
        mutableStateOf(uiState.serverHostName)
    }
    var username by rememberSaveable(uiState.username) {
        mutableStateOf(uiState.username)
    }
    val isInputValid = !listId.isNullOrBlank() && !serverHostName.isNullOrBlank() && !username.isNullOrBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        TextField(
            value = username.orEmpty(),
            onValueChange = { username = it },
            singleLine = true,
            placeholder = { Text("Enter username") },
            keyboardOptions = KeyboardOptions(autoCorrect = false, capitalization = KeyboardCapitalization.None)
        )
        TextField(
            value = listId.orEmpty(),
            onValueChange = { listId = it },
            singleLine = true,
            placeholder = { Text("Enter list name") },
        )
        TextField(
            value = serverHostName.orEmpty(),
            onValueChange = { serverHostName = it },
            singleLine = true,
            placeholder = { Text("Enter server host name") },
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Uri,
                capitalization = KeyboardCapitalization.None
            )
        )
        Button(
            onClick = {
                onLoadHamsterList(username.orEmpty(), listId.orEmpty(), serverHostName.orEmpty())
            },
            enabled = isInputValid
        ) {
            Text(text = "Load")
        }
    }
}

@PreviewLightDark
@Composable
fun HomePagePreview() {
    Surface {
        HomePage(
            uiState = HomeUiState(),
            onLoadHamsterList = { _, _, _ -> }
        )
    }
}
