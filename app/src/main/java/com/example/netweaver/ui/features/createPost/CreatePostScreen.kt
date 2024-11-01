package com.example.netweaver.ui.features.createPost

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.netweaver.R
import com.example.netweaver.ui.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Document(
    val name: String,
    val size: Long?,
    val extension: String?
)

fun Context.getFileDetails(uri: Uri): Document? {

    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            if (displayNameIndex != -1) {
                return Document(
                    name = it.getString(displayNameIndex),
                    size = it.getLong(sizeIndex),
                    extension = getFileExtension(uri)
                )
            }
        }
    }
    return null
}

fun Context.getFileExtension(uri: Uri): String? {
    return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        android.webkit.MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri))
    } else {
        uri.path?.substringAfterLast('.')
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit?,
    viewModel: CreatePostViewModel = hiltViewModel()
) {

    val uiState by viewModel.createPostState.collectAsStateWithLifecycle()
    val state = rememberTextFieldState(initialText = "")
    val context = LocalContext.current

    var fileDetails = remember { mutableStateOf<List<Document>>(emptyList()) }

    val mediaResult = remember { mutableStateOf<List<Uri?>>(emptyList()) }
    val documentResult = remember { mutableStateOf<List<Uri?>>(emptyList()) }

    val mediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)) { uris ->
            mediaResult.value = uris
            fileDetails.value = uris.mapNotNull { uri ->
                context.getFileDetails(uri)
            }
        }

    val documentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            documentResult.value = uris
            fileDetails.value = uris.mapNotNull { uri ->
                context.getFileDetails(uri)
            }
        }

    var byteArrayList by remember { mutableStateOf<Result<List<ByteArray?>?>?>(null) }

    LaunchedEffect(mediaResult.value) {
        if (mediaResult.value.isNotEmpty()) {
            byteArrayList = uriToByteArray(context, mediaResult.value.filterNotNull())
        }
    }

    LaunchedEffect(documentResult.value) {
        if (documentResult.value.isNotEmpty()) {
            byteArrayList = uriToByteArray(context, documentResult.value.filterNotNull())
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
                title = {

                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(R.drawable.logo),
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Profile Image"
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Anyone",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            when (byteArrayList) {
                                is Result.Error -> {
                                    Toast.makeText(
                                        context,
                                        "Unknown error occurred. Try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                is Result.Success -> {
                                    val fileExtensions =
                                        fileDetails.value.mapNotNull { it.extension }
                                    viewModel.createPost(
                                        content = state.text.toString(),
                                        byteArrayList = (byteArrayList as Result.Success<List<ByteArray?>?>).data,
                                        fileExtensions = fileExtensions
                                    )
                                    onNavigateBack()
                                }

                                null -> {}
                            }

                        },
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .height(34.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                            disabledContentColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.38f)
                        ),
                        enabled = state.text.isNotEmpty(),
                        shape = CircleShape, elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            "Post",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigateBack()
                        }) {
                        Icon(
                            painterResource(R.drawable.cancel),
                            contentDescription = "Remove item",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                })

        }
    ) { innerPadding ->
        when (uiState) {
            is CreatePostState.Error -> {
                val toast =
                    Toast.makeText(
                        context,
                        (uiState as CreatePostState.Error).message,
                        Toast.LENGTH_SHORT
                    )

                toast.show()
            }

            is CreatePostState.Idle -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .windowInsetsPadding(WindowInsets.ime)
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surface
                        ),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    item {

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            BasicTextField(
                                state = state,
                                textStyle = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                                decorator = { innerTextField ->

                                    if (state.text.isEmpty()) {
                                        Text(
                                            "Share your thoughts...",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontSize = 18.sp
                                            ),
                                            textAlign = TextAlign.Start,
                                            color = MaterialTheme.colorScheme.onTertiary
                                        )
                                    }


                                    innerTextField()
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colorScheme.surface),
                            )

                        }

                        repeat(mediaResult.value.size) { index ->

                            Column(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 18.dp
                                ),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {

                                IconButton(
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.background,
                                            shape = CircleShape
                                        )
                                        .size(32.dp),
                                    onClick = {
                                        mediaResult.value =
                                            mediaResult.value.toMutableList().apply {
                                                removeAt(index)
                                            }
                                    }) {
                                    Icon(
                                        painterResource(R.drawable.cancel),
                                        contentDescription = "Remove item",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 1.25.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .aspectRatio(0.8f)
                                        .clip(
                                            RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    AsyncImage(
                                        model = mediaResult.value[index],
                                        contentDescription = "Attached Image $index",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                        }

                        if (documentResult.value.isNotEmpty()) {
                            fileDetails.value.forEachIndexed { index, item ->

                                Column(
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 18.dp
                                    ),
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .background(
                                                color = Color.Black,
                                                shape = CircleShape
                                            )
                                            .size(32.dp),
                                        onClick = {
                                            documentResult.value =
                                                documentResult.value.toMutableList().apply {
                                                    removeAt(index)
                                                }
                                            fileDetails.value =
                                                fileDetails.value.toMutableList().apply {
                                                    removeAt(index)
                                                }
                                        }) {
                                        Icon(
                                            painterResource(R.drawable.cancel),
                                            contentDescription = "Remove Item",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.padding(6.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.25.dp,
                                                color = MaterialTheme.colorScheme.outline,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(12.dp)
                                            .clip(
                                                RoundedCornerShape(8.dp)
                                            ),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Image(
                                            painter = painterResource(R.drawable.documentlogo),
                                            contentDescription = "Document Logo",
                                            modifier = Modifier.size(48.dp)
                                        )

                                        Column(
                                            horizontalAlignment = Alignment.Start,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = item.name,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )

                                            Text(
                                                text = "${item.size} bytes",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onTertiary
                                            )
                                        }


                                    }
                                }

                            }
                        }


                    }

                    // Bottom Row
                    item {
                        if (mediaResult.value.isEmpty() && documentResult.value.isEmpty()) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .background(MaterialTheme.colorScheme.surface),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        mediaLauncher.launch(
                                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                        )
                                    }) {
                                    Icon(
                                        painterResource(R.drawable.media),
                                        contentDescription = "Insert Image/Video",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        documentLauncher.launch(
                                            arrayOf("application/pdf")
                                        )
                                    }) {
                                    Icon(
                                        painterResource(R.drawable.document),
                                        contentDescription = "Insert Document",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                    )
                                }

                            }

                        }
                    }
                }
            }

            CreatePostState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            CreatePostState.Success -> {
                onNavigateBack()
                Toast.makeText(context, "Successfully created the post.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

suspend fun uriToByteArray(context: Context, uriList: List<Uri>): Result<List<ByteArray?>> {
    return withContext(Dispatchers.IO) {

        try {

            val byteArrayList = uriList.mapNotNull { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.buffered()?.use { it.readBytes() }
            }

            Result.Success(byteArrayList)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}