package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.ui.components.FlashCardTopic
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme
import com.sugardevs.flashcards.ui.viewModels.HomeScreenViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val homeScreenUiState by homeScreenViewModel.homeUiState.collectAsState()
    val gradientColors = listOf(Color(0xFFB021EE), Color(0xFF11DBFF), Color(0xFFD81B60))

    val cardGradients = listOf(
        Color(0xFF3DDC84) to Color(0xFF2B9D5C),
        Color(0xFF42A5F5) to Color(0xFF00447F),
        Color(0xFF7E57C2) to Color(0xFF5E35B1),
        Color(0xFF740F90) to Color(0xFFB425B2)
    )

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 24.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Hi ${homeScreenUiState.userName},",
                    fontSize = 40.sp,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(colors = gradientColors)
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                homeScreenUiState.subjects.take(4).chunked(2).forEachIndexed { rowIndex, rowSubjects ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowSubjects.forEachIndexed { colIndex, subject ->
                            val (startColor, endColor) = cardGradients[
                                (rowIndex * 2 + colIndex) % cardGradients.size
                            ]

                            FlashCardTopic(
                                subject = subject,
                                gradientStartColor = startColor,
                                gradientEndColor = endColor,
                                onCardClick = { /* TODO: handle subject click */ }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun HomeScreenPreview() {
    FlashCardsTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun HomeScreenPreviewDarkTheme() {
    FlashCardsTheme(darkTheme = true) {
        HomeScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Home Screen With Scaffold Preview")
@Composable
fun HomeScreenWithScaffoldPreview() {
    FlashCardsTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Home Screen Preview") }) }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                HomeScreen()
            }
        }
    }
}
