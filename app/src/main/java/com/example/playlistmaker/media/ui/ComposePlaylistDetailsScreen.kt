package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.NoTracksToShareDialog
import com.example.playlistmaker.compose.PlaylistDeleteConfirmationDialog
import com.example.playlistmaker.compose.TrackRemovingConfirmationDialog
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.yp_bg_dark
import com.example.playlistmaker.main.ui.ui.theme.yp_light_gray
import com.example.playlistmaker.media.ui.stateInterfaces.TrackListState
import com.example.playlistmaker.player.ui.PlaylistElementMini
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaylistDetailsScreen(
    incomingPlaylist: Playlist,
    navHostController: NavHostController,
    playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel(),
) {
    val playlistTracks = playlistDetailsViewModel.listState.collectAsState().value
    var playlist: Playlist by remember { mutableStateOf(incomingPlaylist) }
    var totalDuration by remember { mutableIntStateOf(0) }
    var showMenuSheet by remember { mutableStateOf(false) }
    var isPlaylistDeleteDialogVisible by remember { mutableStateOf(false) }
    var isTrackDeleteDialogVisible by remember { mutableStateOf(false) }
    var isNoTracksDialogVisible by remember { mutableStateOf(false) }
    var trackToDelete: Track? = null
    val sheetStateScaffold = rememberBottomSheetScaffoldState()
    val sheetStateModal = rememberModalBottomSheetState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    //bottomSheetPeekHeight calculation
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val density = LocalDensity.current
    var rowBottomDp by remember { mutableStateOf(0.dp) }

    LaunchedEffect(key1 = playlistTracks) {
        playlistDetailsViewModel.getPlaylistTracks(playlist)
        when (playlistTracks) {
            is TrackListState.Filled -> {
                playlist = playlist.copy(
                    tracks = playlistTracks.tracklist,
                    count = playlistTracks.tracklist.size
                )
                totalDuration = playlistTracks.tracklist.sumOf { it.trackTime }
                Log.i("compose", "$totalDuration секунд")
            }

            is TrackListState.Empty -> {
                playlist = playlist.copy(
                    tracks = playlistTracks.tracklist,
                    count = playlistTracks.tracklist.size
                )
                totalDuration = playlistTracks.tracklist.sumOf { it.trackTime }
                Log.i("compose", "$totalDuration секунд")
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                isIconNeeded = true, text = "", onClick = { navHostController.popBackStack() })
        }) {
        Column(
            Modifier
                .background(color = yp_light_gray)
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            AsyncImage(
                model = playlist.coverUrl,
                contentDescription = "Обложка плейлиста",
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = playlist.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = Typography.titleLarge,
                color = yp_bg_dark
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = playlist.description ?: "",
                style = Typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = yp_bg_dark
            )
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat(
                        "mm минут${
                            when ((totalDuration / 1000 / 60) % 10) {
                                1 -> "а"
                                in 2..4 -> "ы"
                                else -> ""
                            }
                        }",
                        Locale.getDefault()
                    ).format(totalDuration),
                    style = Typography.titleMedium, color = yp_bg_dark
                )
                Icon(
                    modifier = Modifier.padding(horizontal = 9.dp),
                    painter = painterResource(R.drawable.ic_tracks_divider),
                    contentDescription = null,
                    tint = yp_bg_dark
                )
                Text(
                    text = when (playlist.tracks.size % 10) {
                        1 -> "${playlist.tracks.size} трек"
                        in 2..4 -> "${playlist.tracks.size} трека"
                        else -> "${playlist.tracks.size} треков"
                    },
                    style = Typography.titleMedium,
                    color = yp_bg_dark
                )
            }
            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        val position: Offset = coordinates.positionInRoot()
                        rowBottomDp =
                            with(density) { (position.y + coordinates.size.height).toDp() }
                    }
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            onClick = {
                                if (playlist.tracks.isEmpty()) {
                                    isNoTracksDialogVisible = true
                                } else {
                                    playlistDetailsViewModel.onShareClick(
                                        context = context, playlist = playlist
                                    )
                                }
                            }),
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null,
                    tint = yp_bg_dark
                )
                Spacer(Modifier.width(40.dp))
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            onClick = { showMenuSheet = true }),
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = yp_bg_dark
                )
            }
        }
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxWidth(),
            scaffoldState = sheetStateScaffold,
            sheetContent = {
                if (playlist.tracks.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(playlist.tracks.size) { index ->
                            val track = playlist.tracks[index]
                            TrackElement(track = track, onClick = {
                                val jsonTrack = JsonConverter.trackToJson(track)
                                val encodedJson = URLEncoder.encode(jsonTrack, "UTF-8")
                                navHostController.navigate(
                                    route = "${NavRoutes.Player.route}/$encodedJson"
                                )
                            }, onLongClick = {
                                trackToDelete = track
                                isTrackDeleteDialogVisible = true
                            })
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        PlaceholderError(Errors.NoTracksInPlaylist)
                    }
                }
            },
            sheetPeekHeight = if ((screenHeightDp - rowBottomDp) > 50.dp) screenHeightDp - rowBottomDp else 50.dp
        ) {}
        TrackRemovingConfirmationDialog(
            isVisible = isTrackDeleteDialogVisible,
            onDismissRequest = { isTrackDeleteDialogVisible = false },
            onConfirmation = {
                scope.launch {
                    trackToDelete?.let {
                        playlistDetailsViewModel.removeTrackFromPlaylist(
                            track = it, playlist = playlist
                        )
                        playlistDetailsViewModel.getPlaylistTracks(playlist)
                    }
                    trackToDelete = null
                }
                isTrackDeleteDialogVisible = false
            },
        )
        if (showMenuSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetStateModal,
                onDismissRequest = { showMenuSheet = false }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    PlaylistElementMini(
                        playlist = playlist,
                        onClick = {},
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp)
                            .height(61.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .clickable {
                                if (playlist.tracks.isEmpty()) {
                                    isNoTracksDialogVisible = true
                                } else {
                                    playlistDetailsViewModel.onShareClick(
                                        context = context, playlist = playlist
                                    )
                                }
                            }, style = Typography.bodySmall, text = "Поделиться"
                    )
                    Text(
                        modifier = Modifier
                            .height(61.dp)
                            .padding(start = 16.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .clickable {
                                val jsonPlaylist = JsonConverter.playlistToJson(playlist)
                                val encodedJson = URLEncoder.encode(jsonPlaylist, "UTF-8")
                                navHostController.navigate(route = "${NavRoutes.NewPlaylistPage.route}/$encodedJson")
                            },
                        style = Typography.bodySmall,
                        text = "Редактировать информацию"
                    )
                    Text(
                        modifier = Modifier
                            .height(61.dp)
                            .padding(start = 16.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .clickable {
                                isPlaylistDeleteDialogVisible = true
                            }, style = Typography.bodySmall, text = "Удалить плейлист"
                    )
                }
            }
        }

        PlaylistDeleteConfirmationDialog(
            visible = isPlaylistDeleteDialogVisible,
            onDismissRequest = { isPlaylistDeleteDialogVisible = false },
            onConfirmation = {
                playlistDetailsViewModel.deletePlaylist(playlist)
                showMenuSheet = false
                isPlaylistDeleteDialogVisible = false
                navHostController.popBackStack()
            }
        )
        NoTracksToShareDialog(
            visible = isNoTracksDialogVisible,
            onDismissRequest = { isNoTracksDialogVisible = false }
        )
    }
}