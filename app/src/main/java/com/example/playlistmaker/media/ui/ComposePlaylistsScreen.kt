package com.example.playlistmaker.media.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.playlistInfo
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaylistsScreen(
    navHostController: NavHostController,
    playlistsViewModel: PlaylistsViewModel = koinViewModel(),
) {
    val playerState by playlistsViewModel.playlistState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBaseButton(
            onClick = { navHostController.navigate(route = NavRoutes.NewPlaylistPage.route) },
            text = "Создать плейлист"
        )
        when (playerState) {
            is PlaylistState.EmptyList -> {
                PlaceholderError(Errors.NoPlaylists)
            }

            is PlaylistState.FullList -> {
                val list = (playerState as PlaylistState.FullList).playlist
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp)
                ) {
                    items(list.size) { index ->
                        PlaylistElement(list[index])
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistElement(playlist: Playlist) {
    val imageUri = playlist.coverUrl?.toUri()
    Column(
        modifier = Modifier.size(height = 196.dp, width = 160.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Обложка плейлиста",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_connection_problem)
        )
        Text(
            text = playlist.name,
            style = playlistInfo
        )
        Text(
            text = "${playlist.count} треков",
            style = Typography.labelSmall,
        )
    }
}

@ThemePreviews

@Composable
private fun PlaylistPreview() {
    PlaylistMakerTheme {
        Surface {
            PlaylistElement(
                playlist = Playlist(
                    id = 2,
                    name = "My playlist",
                    description = "Discription od a playlist",
                    coverUrl = "https://cs15.pikabu.ru/video/2024/06/14/1718364391216151475_375a057d_1168x864.jpg",
                    tracks = emptyList(),
                    count = 3
                )
            )
        }
    }
}