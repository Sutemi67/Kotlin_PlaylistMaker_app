package com.example.playlistmaker.main.ui.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val darkScheme = darkColorScheme(
    primary = yp_primary_light,
    onPrimary = Color(0xFFFFFFFF),
    background = yp_bg_dark,
    onBackground = Color(0xFFFFFFFF),
    surface = yp_surface_dark,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFAEAFB4),
    onSurfaceVariant = Color(0xFFFFFFFF),
    error = Color(0xFFF56B6C),
    primaryContainer = yp_primary_container_dark,
    secondaryContainer = Color(0xFFFFFFFF),
    onPrimaryContainer = yp_onPrimary_container_dark
)

private val lightScheme = lightColorScheme(
    primary = yp_primary_light,
    onPrimary = Color(0xFFFFFFFF),
    background = yp_bg_light,
    onBackground = Color(0xFF000000),
    surface = yp_surface_light,
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFAEAFB4),
    onSurfaceVariant = Color(0xFFFFFFFF),
    error = Color(0xFFF56B6C),
    primaryContainer = yp_primary_container_light,
    secondaryContainer = Color(0xFF000000),
    onPrimaryContainer = yp_onPrimary_container_light,
)

@Composable
fun customButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }



    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
