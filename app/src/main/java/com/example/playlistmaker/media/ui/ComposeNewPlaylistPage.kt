package com.example.playlistmaker.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme

@Composable
fun NewPlaylistPage(
    onBackClick: () -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(
                isIconNeeded = true,
                text = "Новый плейлист",
                onClick = onBackClick,
            )
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(20.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Gray,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                cap = StrokeCap.Butt,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(40.dp.toPx(), 40.dp.toPx()),
                                    phase = 0f
                                )
                            ),
                            cornerRadius = CornerRadius(100f)
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.add_image),
                    contentDescription = null
                )
            }
            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it }
            )
            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PlaylistPreviewPage() {
    PlaylistMakerTheme {
        Surface() {
            NewPlaylistPage() {}
        }
    }
}