package com.example.playlistmaker.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.main.ui.ui.theme.Typography

@Composable
fun PlaylistDetailsScreen(
//    playlist: Playlist
) {
    Scaffold(
        topBar = { AppTopBar(isIconNeeded = true, text = "", onClick = {}) }
    ) { paddings ->
        Column(modifier = Modifier.padding(paddings)) {
//            AsyncImage(
//                model = playlist.coverUrl,
//                contentDescription = "Обложка плейлиста",
//                modifier = Modifier
//                    .size(160.dp)
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(5.dp)),
//                contentScale = ContentScale.Crop,
//                placeholder = painterResource(R.drawable.img_placeholder),
//                error = painterResource(R.drawable.img_placeholder)
//
//            )
            Image(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                painter = painterResource(R.drawable.img_placeholder),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = "Playlist name",
                style = Typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = "Playlist description",
                style = Typography.titleMedium
            )
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "200 minutes",
                    style = Typography.titleMedium
                )
                Icon(
                    modifier = Modifier.padding(horizontal = 9.dp),
                    painter = painterResource(R.drawable.ic_tracks_divider),
                    contentDescription = null
                )
                Text(
                    text = "88 tracks",
                    style = Typography.titleMedium
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            onClick = {}
                        ),
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null
                )
                Spacer(Modifier.width(40.dp))
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            onClick = {}
                        ),
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null
                )
            }

        }
    }
}


@ThemePreviews
@Composable
fun PlaylistDetailsScreenPreview() {
    PlaylistMakerTheme {
        PlaylistDetailsScreen()
    }
}