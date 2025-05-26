package com.sugardevs.flashcards.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

class PdfUploadScreenViewModel: ViewModel() {

    var text by mutableStateOf("")
        private set

    fun onTextChange(newText: String) {
        text = newText
    }

    fun onPdfUploadButtonPress(pdfFile: File) {
       /*TODO*/
    }

}