package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.components.BottomBar
import com.sugardevs.flashcards.ui.components.FlashCardTopic
import com.sugardevs.flashcards.ui.components.TopBar
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme
import com.sugardevs.flashcards.ui.viewModels.HomeScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = viewModel()
) {
    val homeScreenUiState by homeScreenViewModel.homeUiState.collectAsState()

    val gradientColors = listOf(Color(0xFFB021EE), Color(0xFF11DBFF), Color(0xFFD81B60))
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.app_name),
                onBackClick = { /*TODO*/ },
                onProfileClick = { /*TODO*/ }
            )
        },
        bottomBar = {
            BottomBar()
        }

    ) { paddingValues ->
        Surface(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()

        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
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
                            brush = Brush.horizontalGradient(
                                colors = gradientColors
                            )
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        FlashCardTopic(
                            subject = homeScreenUiState.subjects.subject1,
                            gradientStartColor = Color(0xFF3DDC84),
                            gradientEndColor = Color(0xFF2B9D5C),
                            onCardClick = { /*TODO*/ }
                        )
                        FlashCardTopic(
                            subject = homeScreenUiState.subjects.subject2,
                            gradientStartColor = Color(0xFF42A5F5),
                            gradientEndColor = Color(0xFF00447F),
                            onCardClick = { /*TODO*/ }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FlashCardTopic(
                            subject = homeScreenUiState.subjects.subject3,
                            gradientStartColor = Color(0xFF7E57C2),
                            gradientEndColor = Color(0xFF5E35B1),
                            onCardClick = { /*TODO*/ }
                        )
                        FlashCardTopic(
                            subject = homeScreenUiState.subjects.subject4,
                            gradientStartColor = Color(0xFF740F90),
                            gradientEndColor = Color(0xFFB425B2),
                            onCardClick = { /*TODO*/ }

                        )
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
    FlashCardsTheme(
        darkTheme = false
    ) {
        HomeScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun HomeScreenPreviewDarkTheme() {
    FlashCardsTheme(
        darkTheme = true
    ) {
        HomeScreen()
    }
}