package com.example.playlistmaker.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.main.ui.SingleActivityViewModel
import com.example.playlistmaker.main.ui.ui.theme.Typography
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeSettingsScreen(
    singleActivityViewModel: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(true, "Настройки") { onBackClick() } }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            SwitcherRow(vm = singleActivityViewModel)
            TextIconRow(
                text = "Поделиться приложением",
                icon = painterResource(R.drawable.ic_share),
                onClick = { settingsViewModel.onShareClick() }
            )
            TextIconRow(
                text = "Написать в поддержку",
                icon = painterResource(R.drawable.ic_support),
                onClick = { settingsViewModel.onLinkClick() }
            )
            TextIconRow(
                text = "Пользовательское соглашение",
                icon = painterResource(R.drawable.ic_next),
                onClick = { settingsViewModel.onAgreementClick() }
            )
        }
    }
}

private val commonRowsModifier = Modifier
    .fillMaxWidth()
    .padding(start = 16.dp, end = 16.dp)
    .height(61.dp)

@Composable
private fun TextIconRow(text: String, icon: Painter, onClick: () -> Unit) {
    Row(
        modifier = commonRowsModifier.clickable(true, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, modifier = Modifier.padding(5.dp), style = Typography.bodySmall)
        Icon(painter = icon, contentDescription = text, modifier = Modifier.fillMaxHeight())
    }
}

@Composable
private fun SwitcherRow(
    vm: SingleActivityViewModel,
    settingsViewModel: FragmentSettingsViewModel = koinViewModel()
) {
    val darkModeState = vm.viewStates().collectAsState().value

    Row(
        modifier = commonRowsModifier
            .clickable(
                onClick = {
                    vm.toggleTheme()
                    settingsViewModel.onThemeCheckerClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Темная тема", modifier = Modifier.padding(5.dp), style = Typography.bodySmall)
        Switch(
            modifier = Modifier.scale(0.8f),
            checked = darkModeState,
            onCheckedChange = {
                vm.toggleTheme()
                settingsViewModel.onThemeCheckerClick()
            }
        )
    }
}