package com.sugardevs.flashcards.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme
import com.sugardevs.flashcards.ui.viewModels.PdfUploadScreenViewModel
import com.sugardevs.flashcards.utils.FileUtils.copyPdfToCache
import com.sugardevs.flashcards.utils.UploadUiState


@Composable
fun PdfUploadScreen(
    pdfUploadScreenViewModel: PdfUploadScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = pdfUploadScreenViewModel.uiState

    // Launcher for picking a PDF file
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                val file = copyPdfToCache(uri, context)
                file?.let { pdfUploadScreenViewModel.onPdfUploadButtonPress(it) }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Upload a PDF or enter a topic to generate flashcards.",
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
        )

        // PDF Upload Button
        IconButton(onClick = {
            pdfPickerLauncher.launch(arrayOf("application/pdf"))
        }) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_circle_outline_24),
                contentDescription = stringResource(R.string.upload_pdf),
                modifier = Modifier.size(64.dp)
            )
        }

        // Text input and send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = pdfUploadScreenViewModel.text,
                onValueChange = pdfUploadScreenViewModel::onTextChange,
                modifier = Modifier.weight(1f),
                label = { Text("Enter topic") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            pdfUploadScreenViewModel.uploadTopic(pdfUploadScreenViewModel.text)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Send,
                            contentDescription = stringResource(R.string.send)
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is UploadUiState.Loading -> CircularProgressIndicator()
            is UploadUiState.Success -> {
                Text("Topic: ${uiState.response.topic}", style = MaterialTheme.typography.titleMedium)
                uiState.response.points.forEach { point ->
                    Log.d("Point", point)
                    Text("• $point")
                }
            }
            is UploadUiState.Error -> Text("Error: ${uiState.message}", color = Color.Red)
            UploadUiState.Idle -> {}
        }
    }
}




@Preview(showBackground = true, name = "Light Mode Preview")
@Composable
fun PdfUploadScreenPreview() {
    FlashCardsTheme {
        PdfUploadScreen()
    }
}

@Preview(showBackground = true, name = "Dark Mode Preview")
@Composable
fun PdfUploadScreenPreviewDark() {
    FlashCardsTheme(darkTheme = true) {
        PdfUploadScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Scaffold Layout Preview")
@Composable
fun PdfUploadScreenWithScaffoldPreview() {
    FlashCardsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Upload PDF") }
                )
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                PdfUploadScreen()
            }
        }
    }
}
