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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.main.ui.ui.theme.Typography
import com.example.playlistmaker.main.ui.ui.theme.yp_gray
import com.example.playlistmaker.main.ui.ui.theme.yp_light_gray
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
    val scope = rememberCoroutineScope()
    var searchJob: Job? = null
    var isTracklistVisible by remember { mutableStateOf(true) }
    var isTextVisible by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var historySearchKey by remember { mutableIntStateOf(0) }

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
                    .padding(horizontal = 10.dp),
                colors = TextFieldDefaults.colors().copy(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = yp_light_gray,
                    focusedContainerColor = yp_light_gray,
                ),
                textStyle = Typography.bodySmall,
                value = inputText,
                onValueChange = {
                    inputText = it
                    if (it.isEmpty()) {
                        searchJob?.cancel()
                        trackList = viewModel.getHistory()
                        isTextVisible = true
                        isTracklistVisible = true

                    } else {
                        searchJob?.cancel()
                        searchJob = scope.launch {
                            delay(3000L)
                            loading = true
                            isTracklistVisible = false
                            trackList = viewModel.searchAction(it)
                            loading = false
                            isTracklistVisible = true
                            isTextVisible = false
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
                            loading = false
                            isTextVisible = true
                            historySearchKey++
                            isTracklistVisible = true
                        },
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = null,
                        tint = yp_gray
                    )
                },
                shape = RoundedCornerShape(15.dp)
            )
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 140.dp)
                        .width(44.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (trackList.isNotEmpty() && !loading) {
                    Column {
                        if (isTextVisible) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                text = "Вы искали",
                                textAlign = TextAlign.Center
                            )
                        }
                        LazyColumn {
                            items(trackList.size) { index ->
                                TrackElement(
                                    track = trackList[index],
                                    onClick = {
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
                } else if (!loading) {
                    PlaceholderError(Errors.SearchNothingFound)
                }
            }
        }
    }
}