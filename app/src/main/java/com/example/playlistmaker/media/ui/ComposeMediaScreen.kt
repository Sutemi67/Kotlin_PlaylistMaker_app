package com.example.playlistmaker.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.compose.formatTime
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableMediaScreen(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val titles = listOf("Избранные треки", "Плейлисты")
    val pagerState = rememberPagerState(pageCount = { titles.size })

    Scaffold(
        topBar = { AppTopBar(false, "Медиатека") {} }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) })
                }
            }
            HorizontalPager(
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> FavouritesTracksScreen(navController = navController)
                    1 -> PlaylistsScreen(navController)
                }
            }
        }
    }
}

@Composable
fun FavouritesTracksScreen(
    viewModel: FavouritesViewModel = koinViewModel(),
    navController: NavHostController
) {
    val tracks = viewModel.favouriteTracks.collectAsState().value

    if (tracks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PlaceholderError(error = Errors.NoFavourites)
        }
    } else {
        LazyColumn {
            items(tracks.size) { index ->
                TrackElement(
                    navController = navController,
                    track = tracks[index]
                )
            }
        }
    }
}




@Composable
fun TrackElement(
    navController: NavHostController,
    track: Track
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val jsonTrack = JsonConverter.trackToJson(track)
                val encodedJson = URLEncoder.encode(jsonTrack, "UTF-8")
                navController.navigate(
                    route = "${NavRoutes.Player.route}/$encodedJson"
                )
            }
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

@Composable
fun PlaceholderError(error: Errors) {

    var isVisible = false
    lateinit var text: String
    lateinit var image: Painter

    when (error) {
        Errors.NoFavourites -> {
            text = "Ваши медиатека пуста"
            image = painterResource(R.drawable.img_nothing_found_light)
        }

        Errors.NoPlaylists -> {
            text = "Вы не создали ни одного плейлиста"
            image = painterResource(R.drawable.img_nothing_found_light)
        }

        Errors.SearchNoConnection -> {
            text = "Не т подключени к интернету"
            image = painterResource(R.drawable.img_connection_problem)
        }

        Errors.SearchNothingFound -> {
            text = "По вашему запросу ничего не найдено"
            image = painterResource(R.drawable.img_nothing_found_light)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = null
        )
        Text(text = text)
    }
}

@ThemePreviews
@Composable
fun TrackElementPreview() {
    PlaylistMakerTheme {
        TrackElement(
            navController = rememberNavController(),
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
            )
        )
    }
}