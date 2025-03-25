package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.NoTracksToShareDialog
import com.example.playlistmaker.compose.PlaylistDeleteConfirmationDialog
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.yp_bg_dark
import com.example.playlistmaker.main.ui.ui.theme.yp_light_gray
import com.example.playlistmaker.player.ui.PlaylistElementMini
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaylistDetailsScreen(
    playlist: Playlist,
    navHostController: NavHostController,
    playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel()
) {
    var showMenuSheet by remember { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var isNoTracksDialogVisible by remember { mutableStateOf(false) }
    val sheetStateScaffold = rememberBottomSheetScaffoldState()
    val sheetStateModal = rememberModalBottomSheetState()
//    val tracklist = playlistDetailsViewModel.playlist.collectAsState().value
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                isIconNeeded = true,
                text = "",
                onClick = { navHostController.popBackStack() })
        }
    ) {
        Column(
            Modifier
                .background(color = yp_light_gray)
                .fillMaxSize()
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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = playlist.name,
                style = Typography.titleLarge,
                color = yp_bg_dark
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = playlist.description ?: "",
                style = Typography.titleMedium,
                color = yp_bg_dark
            )
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "200 minutes",
                    style = Typography.titleMedium,
                    color = yp_bg_dark
                )
                Icon(
                    modifier = Modifier.padding(horizontal = 9.dp),
                    painter = painterResource(R.drawable.ic_tracks_divider),
                    contentDescription = null,
                    tint = yp_bg_dark
                )
                Text(
                    text = "${playlist.count} tracks",
                    style = Typography.titleMedium,
                    color = yp_bg_dark
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
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
                                        context = context,
                                        playlist = playlist
                                    )
                                }
                            }
                        ),
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = null,
                    tint = yp_bg_dark
                )
                Spacer(Modifier.width(40.dp))
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            onClick = { showMenuSheet = true }
                        ),
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = yp_bg_dark
                )
            }
        }
        BottomSheetScaffold(
            scaffoldState = sheetStateScaffold,
            sheetContent = {
                if (playlist.tracks.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(playlist.tracks.size) { index ->
                            TrackElement(
                                navController = navHostController,
                                track = playlist.tracks[index]
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        PlaceholderError(Errors.NoTracksInPlaylist)
                    }
                }
            },
            sheetPeekHeight = 250.dp
        ) {}
        if (showMenuSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetStateModal,
                onDismissRequest = { showMenuSheet = false }
            ) {
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
                                        context = context,
                                        playlist = playlist
                                    )
                                }
                            },
                        style = Typography.bodySmall,
                        text = "Поделиться"
                    )
                    Text(
                        modifier = Modifier
                            .height(61.dp)
                            .padding(start = 16.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .clickable {
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
                                isDialogVisible = true
                            },
                        style = Typography.bodySmall,
                        text = "Удалить плейлист"
                    )
                }
            }
        }
        PlaylistDeleteConfirmationDialog(
            visible = isDialogVisible,
            onDismissRequest = { isDialogVisible = false },
            onConfirmation = {
                playlistDetailsViewModel.deletePlaylist(playlist)
                showMenuSheet = false
                isDialogVisible = false
                navHostController.popBackStack()
            }
        )
        NoTracksToShareDialog(
            visible = isNoTracksDialogVisible,
            onDismissRequest = { isNoTracksDialogVisible = false }
        )
    }
}
