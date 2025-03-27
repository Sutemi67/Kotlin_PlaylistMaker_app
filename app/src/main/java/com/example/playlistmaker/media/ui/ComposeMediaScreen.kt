package com.example.playlistmaker.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.Errors
import com.example.playlistmaker.main.ui.SingleActivityViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

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
                    1 -> PlaylistsScreen(navHostController = navController)
                }
            }
        }
    }
}


@Composable
fun PlaceholderError(
    error: Errors,
    activityViewModel: SingleActivityViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        activityViewModel.getThemeValue()
    }
    val isDark = activityViewModel.viewStates().collectAsState().value

    val (text, image) = when (error) {
        Errors.NoFavourites -> "Ваши медиатека пуста" to
                if (isDark)
                    painterResource(R.drawable.img_nothing_found_dark)
                else
                    painterResource(R.drawable.img_nothing_found_light)

        Errors.NoPlaylists -> "Вы не создали ни одного плейлиста" to
                if (isDark)
                    painterResource(R.drawable.img_nothing_found_dark)
                else
                    painterResource(R.drawable.img_nothing_found_light)

        Errors.SearchNoConnection -> "Нет подключения к интернету" to
                if (isDark)
                    painterResource(R.drawable.img_connection_problem_dark)
                else
                    painterResource(R.drawable.img_connection_problem_light)

        Errors.SearchNothingFound -> "По вашему запросу ничего не найдено" to
                if (isDark)
                    painterResource(R.drawable.img_nothing_found_dark)
                else
                    painterResource(R.drawable.img_nothing_found_light)

        Errors.NoTracksInPlaylist -> "В плейлисте отсутствуют треки" to
                if (isDark)
                    painterResource(R.drawable.img_nothing_found_dark)
                else
                    painterResource(R.drawable.img_nothing_found_light)

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