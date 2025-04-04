package com.example.playlistmaker.media.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.playlistInfo
import com.example.playlistmaker.media.ui.stateInterfaces.PlaylistState
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder

@Composable
fun PlaylistsScreen(
    navHostController: NavHostController,
    playlistsViewModel: PlaylistsViewModel = koinViewModel(),
) {
    val playerState by playlistsViewModel.playlistState.collectAsState()
    LaunchedEffect(Unit) {
        Log.i("compose", "запуск обновления плейлистов")
        playlistsViewModel.getPlaylists()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBaseButton(
            onClick = {
                val nullPlaylist = ""
                navHostController.navigate(
                    route = "${NavRoutes.NewPlaylistPage.route}/$nullPlaylist",
                )
            },
            text = "Создать плейлист",
            modifier = Modifier.padding(top = 24.dp),
            isEnabled = true
        )
        when (playerState) {
            is PlaylistState.EmptyList -> {
                PlaceholderError(Errors.NoPlaylists)
            }

            is PlaylistState.FullList -> {
                val list = (playerState as PlaylistState.FullList).playlist
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(list.size) { index ->
                        PlaylistElement(list[index], navHostController)
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistElement(
    playlist: Playlist,
    navHostController: NavHostController
) {
    val imageUri = playlist.coverUrl?.toUri()
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp)
            .clickable {
                val jsonPlaylist = JsonConverter.playlistToJson(playlist)
                val encodedJson = URLEncoder.encode(jsonPlaylist, "UTF-8")
                navHostController.navigate(route = "${NavRoutes.PlaylistDetails.route}/$encodedJson") {}
            },
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Обложка плейлиста",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder)
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = playlist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = playlistInfo
        )
        Text(
            text = "${playlist.count} треков",
            style = Typography.labelSmall,
        )
    }
}