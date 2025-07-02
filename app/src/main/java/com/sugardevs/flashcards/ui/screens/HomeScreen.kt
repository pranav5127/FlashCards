package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sugardevs.flashcards.navigation.Cards
import com.sugardevs.flashcards.ui.components.FlashCardTopic
import com.sugardevs.flashcards.ui.viewModels.HomeScreenViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController ,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    userName: String
) {
    val homeScreenUiState by homeScreenViewModel.homeUiState.collectAsState()

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
                    text = "Hi ${userName},",
                    fontSize = 30.sp,
                   color = MaterialTheme.colorScheme.onSurface
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
                                onCardClick = {
                                    navController.navigate(Cards(subject.topicId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
