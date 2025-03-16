package com.example.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.MusicServiceConnection
import com.example.playlistmaker.app.PlayerService
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import com.example.playlistmaker.search.domain.models.Track

@Composable
fun ComposePlayerScreen(
    viewModel: PlayerViewModel,
    screenSettings: NavRoutes,
    track: Track,
    onBackClick: () -> Unit
) {
    val state = rememberScrollState()
    val context = LocalContext.current
    val serviceConnection = remember { MusicServiceConnection(viewModel) }
    val playerState by viewModel.playerStateAsState.collectAsState()

    DisposableEffect(Unit) {
        val intent = Intent(context, PlayerService::class.java).apply {
            putExtra("song_url", track.previewUrl)
        }
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        onDispose {
            context.unbindService(serviceConnection)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                isIconNeeded = screenSettings.isIcon,
                text = screenSettings.name,
                onClick = { onBackClick() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(state)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                model = track.artworkUrl100,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder)
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                text = track.trackName
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = track.artistName
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_playlist_button),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable { viewModel.onPlayerButtonClicked() },
                    painter = if (playerState is PlayerState.Playing)
                        painterResource(R.drawable.pauseIcon)
                    else painterResource(
                        R.drawable.playIcon
                    ),
                    contentDescription = null,
                )
                Icon(
                    painter = if (!track.isFavourite)
                        painterResource(R.drawable.like_button)
                    else painterResource(R.drawable.like_button_active),
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = track.trackTime.toString(),
                textAlign = TextAlign.Center
            )
            TextRow("Длительность", track.trackTime.toString())
            TextRow("Альбом", track.collectionName)
            TextRow("Год", track.releaseDate ?: "-")
            TextRow("Жанр", track.primaryGenreName)
            TextRow("Страна", track.country)
        }
    }
}

@Composable
private fun TextRow(leftText: String, rightText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(leftText)
        Text(rightText)
    }
}

@ThemePreviews
@Composable
private fun PlayerPreview() {
}