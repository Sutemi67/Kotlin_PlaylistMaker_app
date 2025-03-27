package com.example.playlistmaker.media.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.app.database.domain.model.Playlist
import com.example.playlistmaker.compose.AppBaseButton
import com.example.playlistmaker.compose.AppTopBar
import com.example.playlistmaker.compose.JsonConverter
import com.example.playlistmaker.compose.NavRoutes
import com.example.playlistmaker.compose.PlaylistEditingDialog
import com.example.playlistmaker.compose.fDp2Px
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

@Composable
fun NewPlaylistPage(
    newPlaylistViewModel: NewPlaylistViewModel,
    navHostController: NavHostController,
    playlistForEdit: Playlist?
) {

    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var shouldShowDialog by remember { mutableStateOf(false) }
    var savedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    if (playlistForEdit != null) {
        nameText = playlistForEdit.name
        descriptionText = playlistForEdit.description ?: ""
        savedImageUri = playlistForEdit.coverUrl?.toUri()
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            getPermissions(context, it)
            savedImageUri = saveImageToPrivateStorage(context, it)
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    PlaylistEditingDialog(
        visible = shouldShowDialog,
        onConfirmation = { navHostController.popBackStack() },
        onDismissRequest = { shouldShowDialog = false }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppTopBar(
                isIconNeeded = true,
                text = "Новый плейлист",
                onClick = {
                    if (nameText.isNotEmpty() || descriptionText.isNotEmpty()) {
                        shouldShowDialog = true
                    } else {
                        navHostController.popBackStack()
                    }
                })
        })
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(20.dp)
                    .clickable {
                        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    .drawBehind {
                        if (savedImageUri == null)
                            drawRoundRect(
                                color = Color.Gray,
                                style = Stroke(
                                    width = 1.dp.toPx(),
                                    cap = StrokeCap.Butt,
                                    pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(40.dp.toPx(), 40.dp.toPx()),
                                        phase = 0f
                                    )
                                ),
                                cornerRadius = CornerRadius(100f)
                            )
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (savedImageUri == null) {
                    Image(
                        painter = painterResource(R.drawable.add_image),
                        contentDescription = null
                    )
                } else {
                    AsyncImage(
                        model = savedImageUri,
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                pickMediaLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp),
                value = nameText,
                label = { Text("Название*") },
                shape = RoundedCornerShape(7.dp),
                onValueChange = { nameText = it }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                value = descriptionText,
                label = { Text("Описание") },
                shape = RoundedCornerShape(7.dp),
                onValueChange = { descriptionText = it }
            )
            Spacer(Modifier.weight(1f))
            AppBaseButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 32.dp)
                    .background(
                        color = if (nameText.isEmpty())
                            MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.primaryContainer,
                        shape = object : Shape {
                            override fun createOutline(
                                size: Size,
                                layoutDirection: LayoutDirection,
                                density: Density
                            ) =
                                Outline.Rounded(
                                    RoundRect(
                                        cornerRadius = CornerRadius(8f.fDp2Px),
                                        rect = Rect(size = size, offset = Offset(0f, 0f))
                                    )
                                )
                        },
                    ),
                text = "Создать",
                isEnabled = nameText.isNotEmpty(),
                onClick = {
                    if (playlistForEdit == null) {
                        newPlaylistViewModel.addPlaylist(
                            name = nameText,
                            description = descriptionText,
                            image = savedImageUri.toString(),
                            onResult = {
                                if (it) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Альбом успешно добавлен")
                                    }
                                    navHostController.popBackStack()
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Такие название альбома уже используется")
                                    }
                                    Log.e("compose", "Не добавилось")
                                }
                            }
                        )
                    } else {
                        val playlist = playlistForEdit.copy(
                            name = nameText,
                            description = descriptionText,
                            coverUrl = savedImageUri.toString()
                        )
                        newPlaylistViewModel.updatePlaylist(playlist = playlist)

                        val jsonPlaylist = JsonConverter.playlistToJson(playlist)
                        val encodedJson = URLEncoder.encode(jsonPlaylist, "UTF-8")
                        navHostController.navigate(route = "${NavRoutes.PlaylistDetails.route}/$encodedJson") {
                            popUpTo(
                                navHostController.currentBackStackEntry?.destination?.route
                                    ?: return@navigate
                            ) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}

private fun getPermissions(context: Context, uri: Uri) {
    val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    try {
        context.contentResolver.takePersistableUriPermission(uri, takeFlags)
    } catch (e: SecurityException) {
        Log.e("DATABASE", "Persistable permission not supported for URI: $uri, ${e.message}")
    }
}

private fun saveImageToPrivateStorage(context: Context, uri: Uri): Uri {
    // Определяем путь к каталогу для хранения изображений приложения
    val directory = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "playlists_covers"
    )
    if (!directory.exists()) directory.mkdirs()

    // Генерируем имя файла с текущей меткой времени
    val file = File(directory, "${System.currentTimeMillis()}_cover.jpg")
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            // Декодируем и сжимаем изображение
            BitmapFactory.decodeStream(inputStream)?.compress(
                Bitmap.CompressFormat.JPEG,
                30,
                outputStream
            )
        }
    }
    return file.toUri()
}