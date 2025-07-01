package com.sugardevs.flashcards.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sugardevs.flashcards.ui.components.BottomBar
import com.sugardevs.flashcards.ui.components.SearchBar
import com.sugardevs.flashcards.ui.components.TopBar
import com.sugardevs.flashcards.ui.viewModels.CardsScreenViewModel

@Composable
fun AppScaffold(navController: NavHostController, startDestination: Any) {
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
    if (!displayTopicId.isNullOrBlank()) lastTopicId = displayTopicId

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

    val isAuthRoute =
        currentRouteString == SignIn::class.qualifiedName || currentRouteString == SignUp::class.qualifiedName

    val textFieldState: TextFieldState = rememberTextFieldState()
    val cardsViewModel: CardsScreenViewModel = hiltViewModel()
    val topicSearchResults by cardsViewModel.topicSearchResults.collectAsState()
    val searchQuery = textFieldState.text.toString()


    LaunchedEffect(searchQuery) {
        cardsViewModel.searchTopics(searchQuery)
    }

    Scaffold(
        topBar = {
            if (!isAuthRoute) {
                when (topBarTitle) {
                    "Card Topics" -> SearchBar(
                        textFieldState = textFieldState,
                        onSearch = { cardsViewModel.searchTopics(searchQuery) },
                        searchResults = topicSearchResults,
                        onResultClick = { navController.navigate(Cards(topicId = it)) }
                    )

                    "Exam" -> SearchBar(
                        textFieldState = textFieldState,
                        onSearch = {},
                        searchResults = topicSearchResults,
                        onResultClick = { navController.navigate(Question(topicId = it)) }
                    )

                    else -> TopBar(
                        title = topBarTitle,
                        showBackButton = navController.previousBackStackEntry != null,
                        onBackClick = { navController.popBackStack() },
                        modifier = Modifier,
                        onProfileClick = {
                            navController.navigate(Profile) {
                                launchSingleTop = true
                                popUpTo(Profile) { inclusive = false }
                            }
                        },
                    )
                }
            } else null
        },
        bottomBar = {
            val isTopLevel = listOf(
                Home::class.qualifiedName,
                Cards::class.qualifiedName,
                PdfUpload::class.qualifiedName,
                Exam::class.qualifiedName,
                CardGrid::class.qualifiedName,
                ExamGrid::class.qualifiedName
            ).any { it == currentRouteString || currentRouteString?.startsWith(it!!) == true }

            if (isTopLevel) BottomBar(
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
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
