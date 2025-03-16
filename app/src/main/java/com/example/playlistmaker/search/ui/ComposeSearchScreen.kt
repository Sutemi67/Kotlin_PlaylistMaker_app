package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.ThemePreviews
import com.example.playlistmaker.main.ui.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.main.ui.ui.theme.yp_lightGray
import com.example.playlistmaker.media.ui.TrackElement
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeSearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavHostController
) {
    var inputText by remember { mutableStateOf("") }
    var trackList: List<Track> by remember { mutableStateOf(emptyList()) }
    val scope = rememberCoroutineScope()
    var searchJob: Job? = null

    LaunchedEffect(Unit) {
        trackList = viewModel.getHistory()
        Log.i("compose1", "загрузка истории")
    }

    Scaffold(
        topBar = { AppTopBar(false, "Поиск") {} }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .heightIn(min = 50.dp)
                    .background(
                        color = yp_lightGray,
                        shape = RoundedCornerShape(10.dp)
                    ),
                colors = TextFieldDefaults.colors().copy(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
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
                            tint = yp_lightGray
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "Поиск",
                            color = yp_lightGray
                        )
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (trackList.isNotEmpty()) {
                    Column {
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
                                    navController = navController,
                                    track = trackList[index]
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@ThemePreviews
@Composable
fun Kkkdd() {
    PlaylistMakerTheme {
        ComposeSearchScreen(navController = rememberNavController())
    }
}
