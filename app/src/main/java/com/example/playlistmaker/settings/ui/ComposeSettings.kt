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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.styles.regularTextStyle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComposeSettings() {
    val vm = koinViewModel<FragmentSettingsViewModel>()

    Scaffold(topBar = { AppTopBar(false, "Настройки") {} }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SwitcherRow() { vm.onThemeCheckerClick() } //todo проверить функциональность, потому что подача Boolean не ожидалась ранее
            TextIconRow(
                text = "Поделиться приложением",
                icon = painterResource(R.drawable.ic_share),
                onClick = { vm.onShareClick() }
            )
            TextIconRow(
                text = "Написать в поддержку",
                icon = painterResource(R.drawable.ic_support),
                onClick = { vm.onLinkClick() }
            )
            TextIconRow(
                text = "Пользовательское соглашение",
                icon = painterResource(R.drawable.ic_next),
                onClick = { vm.onAgreementClick() }
            )
        }
    }
}

private val rowModifier = Modifier
    .padding(start = 16.dp, end = 12.dp)
    .fillMaxWidth()
    .height(61.dp)

@Composable
private fun TextIconRow(text: String, icon: Painter, onClick: () -> Unit) {
    Row(
        modifier = rowModifier.clickable(true, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text, modifier = Modifier.padding(5.dp), style = regularTextStyle)
        Icon(painter = icon, contentDescription = text, modifier = Modifier.fillMaxHeight())
    }
}

@Composable
private fun SwitcherRow(onClick: (Boolean) -> Unit) {
    Row(
        modifier = rowModifier.clickable(enabled = true, onClick = {}),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Темная тема", modifier = Modifier.padding(5.dp), style = regularTextStyle)
        Switch(checked = false, onCheckedChange = onClick)
    }
}

@Preview
@Composable
fun Hd() {
    ComposeSettings()
}