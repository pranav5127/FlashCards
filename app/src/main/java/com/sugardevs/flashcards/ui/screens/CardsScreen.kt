package com.sugardevs.flashcards.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.components.BottomBar
import com.sugardevs.flashcards.ui.components.FlashCard
import com.sugardevs.flashcards.ui.components.TopBar
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme
import com.sugardevs.flashcards.ui.viewModels.CardsScreenViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardScreen(
    cardsScreenViewModel: CardsScreenViewModel = viewModel()
) {
    val uiState by cardsScreenViewModel.cardsUiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.card_screen),
                onBackClick = { },
                onProfileClick = { }
            )
        },
        bottomBar = {
            BottomBar()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.cards.isNotEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures { _, dragAmount ->
                                        if (dragAmount < -50) {
                                            cardsScreenViewModel.nextCard()
                                        } else if (dragAmount > 50) {
                                            cardsScreenViewModel.prevCard()
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            for (i in 0..1) {
                                FlashCard(
                                    explanation = "",
                                    modifier = Modifier
                                        .width(300.dp)
                                        .height(400.dp)
                                        .offset(x = -(i * 6).dp, y = (i * 8).dp)
                                        .zIndex(-(i + 1).toFloat())
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(400.dp)
                                    .clipToBounds()
                            ) {
                                AnimatedContent(
                                    targetState = uiState.currentCardIndex,
                                    modifier = Modifier.fillMaxSize(),
                                    transitionSpec = {
                                        val direction =
                                            if (targetState > initialState) {
                                                AnimatedContentTransitionScope.SlideDirection.Left
                                            } else {
                                                AnimatedContentTransitionScope.SlideDirection.Right
                                            }

                                        slideIntoContainer(
                                            towards = direction,
                                            animationSpec = tween(300)
                                        ) + fadeIn(animationSpec = tween(300)) togetherWith
                                                slideOutOfContainer(
                                                    towards = direction,
                                                    animationSpec = tween(300)
                                                ) + fadeOut(animationSpec = tween(300))
                                    },
                                    contentAlignment = Alignment.Center,
                                    label = "CardTransition"
                                ) { targetIndex ->
                                    FlashCard(
                                        explanation = uiState.cards[targetIndex],
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { cardsScreenViewModel.prevCard() },
                                enabled = uiState.currentCardIndex > 0
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_arrow_circle_left_24),
                                    contentDescription = stringResource(R.string.previous_card),
                                    modifier = Modifier.size(64.dp)
                                )
                            }

                            IconButton(
                                onClick = { cardsScreenViewModel.nextCard() },
                                enabled = uiState.currentCardIndex < uiState.cards.lastIndex
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_arrow_circle_right_24),
                                    contentDescription = stringResource(R.string.next_card),
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Card Screen Light")
@Composable
fun CardScreenPreview() {
    FlashCardsTheme {
        CardScreen()
    }
}

@Preview(showBackground = true, name = "Card Screen Dark")
@Composable
fun CardScreenPreviewDarkTheme() {
    FlashCardsTheme(darkTheme = true) {
        CardScreen()
    }
}
