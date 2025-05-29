package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme
import com.sugardevs.flashcards.ui.viewModels.PdfUploadScreenViewModel

@Composable
fun PdfUploadScreen(
    onUploadPdfButtonClick: () -> Unit = {},
    pdfUploadScreenViewModel: PdfUploadScreenViewModel = viewModel()
) {
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
        IconButton(
            onClick = { onUploadPdfButtonClick() }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_circle_outline_24),
                contentDescription = stringResource(R.string.upload_pdf),
                modifier = Modifier
                    .height(64.dp)
                    .width(64.dp)
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            TextField(
                value = pdfUploadScreenViewModel.text,
                onValueChange = pdfUploadScreenViewModel::onTextChange,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PdfUploadScreenPreview() {
    FlashCardsTheme {
        PdfUploadScreen()
    }
}


@Preview(showBackground = true)
@Composable
fun PdfUploadScreenPreviewDarkMode() {
    FlashCardsTheme(darkTheme = true) {
        PdfUploadScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "PDF Upload Screen With Scaffold Preview")
@Composable
fun PdfUploadScreenWithScaffoldPreview() {
    FlashCardsTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Upload PDF Preview") }) },
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                PdfUploadScreen()
            }
        }
    }
}