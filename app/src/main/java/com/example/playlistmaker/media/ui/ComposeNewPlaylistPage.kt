package com.example.playlistmaker.media.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.fDp2Px

@Composable
fun NewPlaylistPage(
    newPlaylistViewModel: NewPlaylistViewModel,
    navHostController: NavHostController
) {
    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            AppTopBar(
                isIconNeeded = true,
                text = "Новый плейлист",
                onClick = { navHostController.popBackStack() },
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
            Spacer(Modifier.weight(1f))
            AppBaseButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 32.dp)
                    .background(
                        color = if (nameText.isEmpty())
                            MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.primaryContainer,
                        shape = object : Shape {
                            override fun createOutline(
                                size: Size,
                                layoutDirection: LayoutDirection,
                                density: Density
                            ) =
                                Outline.Rounded(
                                    RoundRect(
                                        cornerRadius = CornerRadius(8f.fDp2Px),
                                        rect = Rect(size = size, offset = Offset(0f, 0f))
                                    )
                                )
                        },
                    ),
                text = "Создать",
                isEnabled = nameText.isNotEmpty(),
                onClick = {
                    newPlaylistViewModel.addPlaylist(
                        name = nameText,
                        description = descriptionText,
                        image = null,
                        onResult = {
                            if (it) {
                                navHostController.popBackStack()
                                Log.i("compose", "добавилось")
                            } else {
                                Log.e("compose", "Не добавилось")
                            }
                        }
                    )
                },
            )
        }
    }
}