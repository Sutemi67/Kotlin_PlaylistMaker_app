package com.example.playlistmaker.main.ui.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController


//universal
val likeFillColor = Color(0xFFF56B6C)
val yp_gray = Color(0xFFAEAFB4)
val yp_light_gray = Color(0xFFE6E8EB)
val yp_white = Color(0xFFFFFFFF)
val yp_transparent = Color(0x00FFFFFF)
val yp_primary = Color(0xFF3772E7)

//dark theme
val yp_bg_dark = Color(0xFF1A1B22)
val yp_surface_dark = Color(0xFF1A1B22)
val yp_primary_container_dark = Color(0xFFFFFFFF)
val yp_onPrimary_container_dark = Color(0xFF1A1B22)
val yp_editTextField_container_color_dark = Color(0xFFFFFFFF)
val yp_editTextField_text_color_dark = Color(0xFF1A1B22)

//light theme
val yp_bg_light = Color(0xFFFFFFFF)
val yp_surface_light = Color(0xFFFFFFFF)
val yp_primary_container_light = Color(0xFF0A1B22)
val yp_onPrimary_container_light = Color(0xFFECECEC)
val yp_editTextField_container_color_light = Color(0xFFE6E8EB)
val yp_editTextField_text_color_light = Color(0xFF1A1B22)

private val darkScheme = darkColorScheme(
    primary = yp_primary,
    onPrimary = Color(0xFFFFFFFF),
    background = yp_bg_dark,
    onBackground = Color(0xFFFFFFFF),
    surface = yp_surface_dark,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFAEAFB4),
    onSurfaceVariant = yp_bg_light,
    error = Color(0xFFF56B6C),
    primaryContainer = yp_primary_container_dark,
    secondaryContainer = Color(0xFFFFFFFF),
    onPrimaryContainer = yp_onPrimary_container_dark,
    surfaceContainerLow = yp_bg_dark, //BottomSheet Modal
)

private val lightScheme = lightColorScheme(
    primary = yp_primary,
    onPrimary = Color(0xFFFFFFFF),
    background = yp_bg_light,
    onBackground = Color(0xFF000000),
    surface = yp_surface_light,
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFAEAFB4),
    onSurfaceVariant = yp_bg_dark,
    error = Color(0xFFF56B6C),
    primaryContainer = yp_primary_container_light,
    secondaryContainer = Color(0xFF000000),
    onPrimaryContainer = yp_onPrimary_container_light,
    surfaceContainerLow = yp_bg_light, // BottomSheet Modal
)

@Composable
fun customEditTextFieldsColors(): TextFieldColors {
    return if (MaterialTheme.colorScheme == lightScheme) {
        TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = yp_editTextField_container_color_light,
            unfocusedContainerColor = yp_editTextField_container_color_light,
            unfocusedTextColor = yp_editTextField_text_color_light,
            focusedTextColor = yp_editTextField_text_color_light
        )
    } else {
        TextFieldDefaults.colors().copy(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = yp_editTextField_container_color_dark,
            unfocusedContainerColor = yp_editTextField_container_color_dark,
            unfocusedTextColor = yp_editTextField_text_color_dark,
            focusedTextColor = yp_editTextField_text_color_dark
        )
    }
}

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
    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false
        )
    } else {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }
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
