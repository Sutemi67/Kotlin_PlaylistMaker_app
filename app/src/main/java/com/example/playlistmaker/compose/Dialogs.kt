package com.example.playlistmaker.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playlistmaker.main.ui.ui.theme.Typography

@Composable
fun PlaylistEditingDialog(
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

@Composable
fun PlaylistDeleteConfirmationDialog(
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
                    text = "Действительно удалить плейлист?"
                )
            },
            text = {
                Text(
                    style = Typography.bodySmall,
                    text = ""
                )
            },
            confirmButton = {
                Button(
                    onClick = { onConfirmation() }
                ) {
                    Text(
                        style = Typography.labelMedium,
                        text = "Удалить"
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

@Composable
fun NoTracksToShareDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(
                    style = Typography.titleLarge,
                    text = ""
                )
            },
            text = {
                Text(
                    style = Typography.bodySmall,
                    text = "В плейлисте отсутствуют треки"
                )
            },
            confirmButton = {
                Button(
                    onClick = { onDismissRequest() }
                ) {
                    Text(
                        style = Typography.labelMedium,
                        text = "Ок"
                    )
                }
            },
        )
    }
}

@Composable
fun TrackRemovingConfirmationDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(
                    style = Typography.titleLarge,
                    text = "Хотите удалить трек?"
                )
            },
            confirmButton = {
                Button(
                    onClick = { onConfirmation() }
                ) {
                    Text(
                        style = Typography.labelMedium,
                        text = "Да"
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest() }
                ) {
                    Text(
                        style = Typography.labelMedium,
                        text = "Нет"
                    )
                }
            }
        )
    }
}