package com.example.playlistmaker.player.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.search.domain.models.Track

@Composable
fun ComposePlayerScreen(
    screenSettings: NavRoutes,
    track: Track
) {
//    val state = rememberScrollableState { }
    Scaffold(
        topBar = {
            AppTopBar(
                isIconNeeded = screenSettings.isIcon,
                text = screenSettings.name,
                onClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
//                .scrollable(state, orientation = Orientation.Vertical)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(5.dp),
                painter = painterResource(R.drawable.img_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
//            AsyncImage(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(5.dp),
//                model = null,
//                contentDescription = null,
//                placeholder = painterResource(R.drawable.img_placeholder)
//            )
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
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    painter = painterResource(R.drawable.playIcon),
                    contentDescription = null
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
    PlaylistMakerTheme {
        ComposePlayerScreen(
            screenSettings = NavRoutes.Player,
            Track(
                trackId = 2,
                previewUrl = null,
                trackName = "Ghbsdfff sdfssdfsdfsdfdfgdfgdfgdfgdfgdfgdfgsdfsdfsdfdf",
                artistName = "dDsfsd DSd",
                trackTime = 2412424,
                artworkUrl100 = null,
                country = "US",
                collectionName = "dfsdf",
                primaryGenreName = "rap",
                releaseDate = null,
                isFavourite = false,
                latestTimeAdded = 234234
            )
        )
    }
}