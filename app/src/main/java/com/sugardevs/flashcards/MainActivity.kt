package com.sugardevs.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme
import com.sugardevs.flashcards.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardsTheme {
             Scaffold { paddingValues ->
                 HomeScreen(modifier = Modifier.padding(paddingValues))
             }
            }
        }
    }
}
