package com.example.playlistmaker.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchContent(answerCode: Int) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        if (answerCode == 0) {
            Text("Вы искали")
            LazyColumn { }
            FilledTonalButton(onClick = {}) {
                Text("Очистить историю")
            }
        } else if (answerCode == 200) {
            PlaceholderNothingFound()
        } else {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Kkdd() {
    SearchContent(1)
}