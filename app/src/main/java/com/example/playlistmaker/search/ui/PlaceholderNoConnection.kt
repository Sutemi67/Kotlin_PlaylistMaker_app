package com.example.playlistmaker.search.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.ThemePreviews

@Composable
fun PlaceholderNoConnection() {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_connection_problem_light),
            contentDescription = "nothing found"
        )
        Text("Проблемы со связью")
        Text("Загрузка не удалась. Проверьте ваше подключение к интернету")
        AppBaseButton(
            text = "Обновить",
            modifier = Modifier,
            onClick = {},
            isEnabled = true
        )
    }
}

@ThemePreviews
@Composable
fun Kkdddd() {
    PlaceholderNoConnection()
}