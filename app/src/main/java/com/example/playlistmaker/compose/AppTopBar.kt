package com.example.playlistmaker.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.main.ui.ui.theme.Typography

@Composable
fun AppTopBar(
    isIconNeeded: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isIconNeeded) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "back icon",
                modifier = Modifier
                    .clickable { onClick }
                    .padding(horizontal = 10.dp)
            )
        }
        Text(
            text = text,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Hdd() {
    AppTopBar(true, "Настройки") {}
}