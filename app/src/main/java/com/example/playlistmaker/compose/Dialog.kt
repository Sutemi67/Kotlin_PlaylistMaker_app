package com.example.playlistmaker.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playlistmaker.main.ui.ui.theme.Typography

@Composable
fun NewPlaylistConfirmationDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(
                    style = Typography.titleLarge,
                    text = "Завершить создание плейлиста?"
                )
            },
            text = {
                Text(
                    style = Typography.bodySmall,
                    text = "Все несохраненные данные будут потеряны"
                )
            },
            confirmButton = {
                Button(
                    onClick = { onConfirmation() }
                ) {
                    Text(
                        style = Typography.labelMedium,
                        text = "Завершить"
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest() }
                ) {
                    Text(
                        style = Typography.labelMedium,
                        text = "Отмена"
                    )
                }
            }
        )
    }
}