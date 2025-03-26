package com.example.playlistmaker.media.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.compose.formatTime
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.search.domain.models.Track
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder


@Composable
fun FavouritesTracksScreen(
    viewModel: FavouritesViewModel = koinViewModel(),
    navController: NavHostController
) {
    val tracks = viewModel.favouriteTracks.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.refreshFavourites()
    }

    if (tracks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PlaceholderError(error = Errors.NoFavourites)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tracks.size) { index ->
                TrackElement(
//                    navController = navController,
                    track = tracks[index],
                    onLongClick = {},
                    onClick = {
                        val jsonTrack = JsonConverter.trackToJson(tracks[index])
                        val encodedJson = URLEncoder.encode(jsonTrack, "UTF-8")
                        navController.navigate(
                            route = "${NavRoutes.Player.route}/$encodedJson"
                        )
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackElement(
//    navController: NavHostController,
    track: Track,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
//                    val jsonTrack = JsonConverter.trackToJson(track)
//                    val encodedJson = URLEncoder.encode(jsonTrack, "UTF-8")
//                    navController.navigate(
//                        route = "${NavRoutes.Player.route}/$encodedJson"
//                    )
                    onClick()
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .padding(5.dp),
            model = track.artworkUrl100,
            contentDescription = null,
            placeholder = painterResource(R.drawable.img_placeholder)
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(
                text = track.artistName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = track.trackName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Icon(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    painter = painterResource(R.drawable.ic_tracks_divider),
                    contentDescription = null,

                    )
                //todo пофиксить разметку нижней строки со временем трека
                Text(
                    modifier = Modifier.weight(1f),
                    text = formatTime(track.trackTime)
                )
            }
        }
        Icon(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(20.dp),
            painter = painterResource(R.drawable.ic_next),
            contentDescription = null
        )
    }
}

@ThemePreviews
@Composable
private fun TrackElementPreview() {
    PlaylistMakerTheme {
        TrackElement(
//            navController = rememberNavController(),
            Track(
                trackId = 2,
                previewUrl = null,
                trackName = "Ghbsdfff sdfssdfsdfsdfdfgdfgdfgdfgdfgdfgdfgsdfsdfsdfdf",
                artistName = "dDsfsd DSd",
                trackTime = 2412424,
                artworkUrl100 = painterResource(R.drawable.img_placeholder).toString(),
//                artworkUrl100 = null,
                country = "US",
                collectionName = "dfsdf",
                primaryGenreName = "rap",
                releaseDate = null,
                isFavourite = false,
                latestTimeAdded = 234234
            ),
            onLongClick = {},
            onClick = {}
        )
    }
}