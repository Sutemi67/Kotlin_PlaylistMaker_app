package com.example.playlistmaker.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.main.ui.ui.theme.yp_lightGray

@Composable
fun ComposeSearch(
    toSettingsClick: () -> Unit
//    vm: ViewModel = koinViewModel<SearchViewModel>()
) {
    var inputText by remember { mutableStateOf("") }

    Scaffold(topBar = { AppTopBar(false, "Поиск") {} }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 10.dp)
                    .background(color = yp_lightGray),
                singleLine = true,
                label = { Text("Поиск") }
            )
            if (true) {//todo написать условие полноты списка
                SearchContent(1)
            }
            ElevatedButton(onClick = toSettingsClick) { }
        }
    }
}

@Preview
@Composable
fun Kkkdd() {
    ComposeSearch() {}
}
