package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.customEditTextFieldsColors
import com.example.playlistmaker.main.ui.ui.theme.yp_gray
import com.example.playlistmaker.media.ui.PlaceholderError
import com.example.playlistmaker.media.ui.TrackElement
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder

@Composable
fun ComposeSearchScreen(
    viewModel: SearchViewModel = koinViewModel(), navController: NavHostController
) {
    var inputText by remember { mutableStateOf("") }
    var trackList: List<Track> by remember { mutableStateOf(emptyList()) }
    var historySearchKey by remember { mutableIntStateOf(0) }

    var uiState = viewModel.uiState.collectAsState().value

    val scope = rememberCoroutineScope()
    var searchJob: Job? = null

    LaunchedEffect(key1 = historySearchKey) {
        trackList = viewModel.getHistory()
        Log.i("compose1", "загрузка истории")
    }

    Scaffold(
        topBar = { AppTopBar(false, "Поиск") {} }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = customEditTextFieldsColors(),
                textStyle = Typography.bodySmall,
                value = inputText,
                onValueChange = {
                    inputText = it
                    if (it.isEmpty()) {
                        searchJob?.cancel()
                        trackList = viewModel.getHistory()
                    } else {
                        searchJob?.cancel()
                        searchJob = scope.launch {
                            delay(3000L)
                            trackList = viewModel.searchAction(it)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                placeholder = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_light),
                            contentDescription = null,
                            tint = yp_gray
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "Поиск",
                            color = yp_gray
                        )
                    }
                },
                trailingIcon = {
                    if (inputText.isNotEmpty()) Icon(
                        modifier = Modifier.clickable {
                            inputText = ""
                            searchJob?.cancel()
                            historySearchKey++
                        },
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = null,
                        tint = yp_gray
                    )
                },
                shape = RoundedCornerShape(15.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                UiState(
                    state = uiState,
                    trackList = trackList,
                    viewModel = viewModel,
                    navController = navController,
                )
            }
        }
    }
}

@Composable
private fun UiState(
    state: UiState,
    trackList: List<Track>,
    viewModel: SearchViewModel,
    navController: NavHostController
) {
    when (state) {
        UiState.EmptyHistory -> {}
        UiState.FullHistory -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "Вы искали",
                    textAlign = TextAlign.Center
                )
                LazyColumn {
                    items(trackList.size) { index ->
                        TrackElement(
                            track = trackList[index],
                            onClick = {
                                viewModel.addTrackInHistory(trackList[index])
                                val jsonTrack = JsonConverter.trackToJson(trackList[index])
                                val encodedJson = URLEncoder.encode(jsonTrack, "UTF-8")
                                navController.navigate(
                                    route = "${NavRoutes.Player.route}/$encodedJson"
                                )
                            },
                            onLongClick = {}
                        )
                    }
                }
                AppBaseButton(
                    text = "Очистить историю",
                    onClick = { viewModel.clearHistory() },
                    modifier = Modifier.padding(10.dp),
                    isEnabled = true
                )
            }
        }

        UiState.FullSearch -> {
            LazyColumn {
                items(trackList.size) { index ->
                    TrackElement(
                        track = trackList[index],
                        onClick = {
                            viewModel.addTrackInHistory(trackList[index])
                            val jsonTrack = JsonConverter.trackToJson(trackList[index])
                            val encodedJson = URLEncoder.encode(jsonTrack, "UTF-8")
                            navController.navigate(
                                route = "${NavRoutes.Player.route}/$encodedJson"
                            )
                        },
                        onLongClick = {}
                    )
                }
            }
        }

        UiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 140.dp)
                    .width(44.dp)
            )
        }

        UiState.NoConnection -> {
            PlaceholderError(Errors.SearchNoConnection)
        }

        UiState.NothingFound -> {
            PlaceholderError(Errors.SearchNothingFound)
        }
    }
}