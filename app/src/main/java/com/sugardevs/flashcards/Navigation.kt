package com.sugardevs.flashcards

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sugardevs.flashcards.ui.components.BottomBar
import com.sugardevs.flashcards.ui.components.ExamQuestions
import com.sugardevs.flashcards.ui.components.SearchBar
import com.sugardevs.flashcards.ui.components.TopBar
import com.sugardevs.flashcards.ui.screens.CardScreen
import com.sugardevs.flashcards.ui.screens.CardsGridScreen
import com.sugardevs.flashcards.ui.screens.ExamGridScreen
import com.sugardevs.flashcards.ui.screens.ExamScreen
import com.sugardevs.flashcards.ui.screens.HomeScreen
import com.sugardevs.flashcards.ui.screens.PdfUploadScreen
import com.sugardevs.flashcards.ui.screens.ProfileScreen
import com.sugardevs.flashcards.ui.viewModels.CardsScreenViewModel
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Cards(val topicId: String)

@Serializable
data class Exam(
    val subject: String,
    val questionCount: Int
)

@Serializable
object PdfUpload

@Serializable
data class Question(val topicId: String)

@Serializable
object CardGrid

@Serializable
object ExamGrid

@Serializable
object Profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRouteString = currentDestination?.route
    val currentArgs = navBackStackEntry?.arguments

    val displayTopicId = when {
        currentRouteString?.startsWith(Cards::class.qualifiedName!!) == true ->
            currentArgs?.getString("topicId")

        currentRouteString?.startsWith(Exam::class.qualifiedName!!) == true ->
            currentArgs?.getString("subject")

        currentRouteString?.startsWith(Question::class.qualifiedName!!) == true ->
            currentArgs?.getString("topicId")

        else -> null
    }

    var lastTopicId by remember { mutableStateOf("") }
    if (!displayTopicId.isNullOrBlank()) {
        lastTopicId = displayTopicId
    }

    val topBarTitle = when {
        currentRouteString == Home::class.qualifiedName -> "Home"
        currentRouteString?.startsWith(Cards::class.qualifiedName!!) == true -> "Flash Cards: $lastTopicId"
        currentRouteString?.startsWith(Exam::class.qualifiedName!!) == true -> "Exam"
        currentRouteString?.startsWith(Question::class.qualifiedName!!) == true -> "Question: $lastTopicId"
        currentRouteString == PdfUpload::class.qualifiedName -> "Upload PDF"
        currentRouteString == CardGrid::class.qualifiedName -> "Card Topics"
        currentRouteString == ExamGrid::class.qualifiedName -> "Exam Topics"
        else -> "Flashcards App"
    }

    val textFieldState: TextFieldState = rememberTextFieldState()

    val cardsViewModel: CardsScreenViewModel = hiltViewModel()
    val topicSearchResults by cardsViewModel.topicSearchResults.collectAsState()

    val searchQuery = textFieldState.text.toString()
    LaunchedEffect(searchQuery) {
        cardsViewModel.searchTopics(searchQuery)
    }

    Scaffold(
        topBar = {
            when {
                currentRouteString != null -> {
                    when (topBarTitle) {
                        "Card Topics" -> {
                            SearchBar(
                                textFieldState = textFieldState,
                                onSearch = { cardsViewModel.searchTopics(searchQuery) },
                                searchResults = topicSearchResults,
                                onResultClick = { selectedTopicId ->
                                    navController.navigate(Cards(topicId = selectedTopicId))
                                }

                            )
                        }

                        "Exam" -> {
                            SearchBar(
                                textFieldState = textFieldState,
                                onSearch = { },
                                searchResults = topicSearchResults,
                                onResultClick = { selectedTopicId ->
                                    navController.navigate(Question(topicId = selectedTopicId))
                                }

                            )
                        }

                        else -> {
                            TopBar(
                                title = topBarTitle,
                                showBackButton = navController.previousBackStackEntry != null,
                                onBackClick = { navController.popBackStack() },
                                modifier = Modifier,
                                onProfileClick = {
                                    navController.navigate(Profile)
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            val isTopLevel = when (currentRouteString) {
                Home::class.qualifiedName,
                Cards::class.qualifiedName,
                PdfUpload::class.qualifiedName,
                Exam::class.qualifiedName,
                CardGrid::class.qualifiedName,
                ExamGrid::class.qualifiedName -> true

                else -> {
                    currentRouteString?.startsWith(Cards::class.qualifiedName!!) == true ||
                            currentRouteString?.startsWith(Exam::class.qualifiedName!!) == true
                }
            }

            if (isTopLevel) {
                BottomBar(
                    onHomeClick = {
                        navController.navigate(Home) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onFlashCardsClick = {
                        navController.navigate(CardGrid) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onUploadPdfClick = {
                        navController.navigate(PdfUpload) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onExamClick = {
                        navController.navigate(ExamGrid) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        composable<Home> {
            HomeScreen(navController = navController)
        }
        composable<Cards> { backStackEntry ->
            val args = backStackEntry.toRoute<Cards>()
            CardScreen(topicId = args.topicId)
        }
        composable<Exam> { backStackEntry ->
            val args = backStackEntry.toRoute<Exam>()
            ExamScreen(
                subject = args.subject,
                questionCount = args.questionCount,
                onExamCardClick = { actualTopicId ->
                    navController.navigate(Question(topicId = actualTopicId))
                }
            )
        }
        composable<Question> { backStackEntry ->
            val args = backStackEntry.toRoute<Question>()
            ExamQuestions(
                topicId = args.topicId,
                onSubmitExam = {
                    navController.popBackStack()
                }
            )
        }
        composable<PdfUpload> {
            PdfUploadScreen(navController = navController)
        }
        composable<CardGrid> {
            CardsGridScreen(
                onCardsClick = { topicId ->
                    navController.navigate(Cards(topicId = topicId))
                },
            )
        }
        composable<ExamGrid> {
            ExamGridScreen(
                onExamClick = { topicId ->
                    navController.navigate(
                        Exam(
                            subject = topicId,
                            questionCount = 10
                        )
                    )
                }
            )
        }
        composable<Profile> {
            ProfileScreen()
        }
    }
}
