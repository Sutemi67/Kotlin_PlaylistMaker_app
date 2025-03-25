package com.example.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.MusicServiceConnection
import com.example.playlistmaker.app.PlayerService
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.likeFillColor
import com.example.playlistmaker.main.ui.ui.theme.playlistInfo
import com.example.playlistmaker.main.ui.ui.theme.yp_gray
import com.example.playlistmaker.media.ui.PlaylistsViewModel
import com.example.playlistmaker.media.ui.stateInterfaces.PlayerState
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposePlayerScreen(
    playerViewModel: PlayerViewModel,
    playlistsViewModel: PlaylistsViewModel = koinViewModel(),
    screenSettings: NavRoutes,
    track: Track,
    navHostController: NavHostController
) {
    val state = rememberScrollState()
    val context = LocalContext.current
    val serviceConnection = remember { MusicServiceConnection(playerViewModel) }
    val playerState = playerViewModel.playerStateAsState.collectAsState().value
    val playlistState = playlistsViewModel.playlistState.collectAsState().value
    val addingTrackStatus = playerViewModel.addingStatus2.collectAsState().value
    var isBottomMenuVisible by remember { mutableStateOf(false) }
    var isTrackFavourite by remember { mutableStateOf(track.isFavourite) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
                onClick = { navHostController.popBackStack() })
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(state)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 26.dp)
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
                    modifier = Modifier.padding(top = 26.dp), text = track.trackName
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp), text = track.artistName
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.clickable {
                            scope.launch {
                                playlistsViewModel.getPlaylists()
                            }
                            isBottomMenuVisible = true
                        },
                        painter = painterResource(R.drawable.add_playlist_button),
                        contentDescription = null,
                        tint = yp_gray
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .clickable { playerViewModel.onPlayerButtonClicked() },
                        painter = if (playerState is PlayerState.Playing) {
                            painterResource(R.drawable.pauseIcon)
                        } else {
                            painterResource(R.drawable.playIcon)
                        },
                        contentDescription = null,
                    )
                    Icon(
                        modifier = Modifier.clickable {
                            playerViewModel.toggleFavourite(track)
                            isTrackFavourite = !isTrackFavourite
                        },
                        painter = if (!isTrackFavourite) {
                            painterResource(R.drawable.like_button)
                        } else {
                            painterResource(R.drawable.like_button_active)
                        },
                        contentDescription = null,
                        tint = if (isTrackFavourite) likeFillColor else yp_gray
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    text = track.trackTime.toString(),
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 30.dp)
            ) {
                TextRow("Длительность", track.trackTime.toString())
                TextRow("Альбом", track.collectionName)
                TextRow("Год", track.releaseDate ?: "-")
                TextRow("Жанр", track.primaryGenreName)
                TextRow("Страна", track.country)
            }
        }
        if (isBottomMenuVisible) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetState,
                onDismissRequest = { isBottomMenuVisible = false }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .height(52.dp),
                        text = "Добавить в плейлист",
                        style = Typography.titleMedium
                    )
                    AppBaseButton(
                        text = "Новый плейлист",
                        onClick = { navHostController.navigate(route = NavRoutes.NewPlaylistPage.route) },
                        modifier = Modifier.padding(bottom = 24.dp),
                        isEnabled = true
                    )

                    val list = when (playlistState) {
                        is PlaylistState.EmptyList -> emptyList<Playlist>()
                        is PlaylistState.FullList -> playlistState.playlist
                    }
                    if (list.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 60.dp),
                        ) {
                            items(list.size) { index ->
                                PlaylistElementMini(
                                    playlist = list[index],
                                    onClick = {
                                        playerViewModel.addInPlaylist(
                                            track = track,
                                            playlist = list[index]
                                        )
                                        isBottomMenuVisible = false
                                        if (addingTrackStatus) {
                                            Toast.makeText(
                                                context,
                                                "success",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "not success",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistElementMini(
    playlist: Playlist,
    onClick: () -> Unit
) {
    val imageUri = playlist.coverUrl?.toUri()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Обложка плейлиста",
            modifier = Modifier
                .padding(start = 13.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder)
        )
        Column {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = playlist.name,
                style = playlistInfo
            )
            Text(
                text = "${playlist.count} треков",
                style = Typography.labelSmall,
            )
        }
    }
}

@Composable
private fun TextRow(leftText: String, rightText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = leftText, color = yp_gray, style = Typography.titleSmall
        )
        Text(
            text = rightText, style = Typography.titleSmall
        )
    }
}

@ThemePreviews
@Composable
private fun PlayerPreview() {
    Column {
        TextRow("Жанр", "Rapa")
        TextRow("Страна", "USA")
    }
}