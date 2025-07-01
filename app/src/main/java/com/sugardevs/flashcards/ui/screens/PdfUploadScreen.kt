package com.sugardevs.flashcards.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.navigation.Cards
import com.sugardevs.flashcards.ui.viewModels.PdfUploadScreenViewModel
import com.sugardevs.flashcards.utils.FileUtils.copyPdfToCache
import com.sugardevs.flashcards.utils.UploadUiState

@Composable
fun PdfUploadScreen(
    navController: NavHostController,
    pdfUploadScreenViewModel: PdfUploadScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = pdfUploadScreenViewModel.uiState
    val navigateToTopic = pdfUploadScreenViewModel.navigateToTopicId

    val snackbarHostState = remember { SnackbarHostState() }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                val file = copyPdfToCache(uri, context)
                file?.let { pdfUploadScreenViewModel.onPdfUploadButtonPress(it) }
            }
        }
    )

    LaunchedEffect(navigateToTopic) {
        navigateToTopic?.let { topic ->
            navController.navigate(Cards(topicId = topic))
            pdfUploadScreenViewModel.onNavigated()
        }
    }

    // Show snackbar on error
    LaunchedEffect(uiState) {
        if (uiState is UploadUiState.Error) {
            snackbarHostState.showSnackbar(uiState.message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Upload a PDF or enter a topic to generate flashcards and quiz.",
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )

            IconButton(onClick = {
                pdfPickerLauncher.launch(arrayOf("application/pdf"))
            }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_circle_outline_24),
                    contentDescription = stringResource(R.string.upload_pdf),
                    modifier = Modifier.size(64.dp)
                )
            }

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
                                imageVector = Icons.AutoMirrored.Filled.Send,
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
                is UploadUiState.Error -> {
                }
                UploadUiState.Idle -> {}
            }
        }
    }
}
