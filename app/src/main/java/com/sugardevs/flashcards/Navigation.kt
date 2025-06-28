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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sugardevs.flashcards.ui.components.*
import com.sugardevs.flashcards.ui.screens.*
import com.sugardevs.flashcards.ui.screens.auth.SignInScreen
import com.sugardevs.flashcards.ui.screens.auth.SignUpScreen
import com.sugardevs.flashcards.ui.viewModels.CardsScreenViewModel
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel
import kotlinx.serialization.Serializable

@Serializable
object SignUp
@Serializable
object SignIn
@Serializable
object Home
@Serializable
data class Cards(val topicId: String)
@Serializable
data class Exam(val subject: String, val questionCount: Int)
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
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    if (isLoading) return

    val startDestination = if (isLoggedIn) Home else SignIn

    AppScaffold(navController = navController, startDestination = startDestination)
}

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
                        onProfileClick = { navController.navigate(Profile) }
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

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(
                tween(
                    300
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(
                tween(300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(tween(300))
        }
    ) {
        composable<SignUp> {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Home) {
                        popUpTo(SignUp) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToSignIn = { navController.navigate(SignIn) }
            )
        }
        composable<SignIn> {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(Home) {
                        popUpTo(SignIn) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToSignUp = { navController.navigate(SignUp) }
            )
        }
        composable<Home> { HomeScreen(navController = navController) }
        composable<Cards> {
            val args = it.toRoute<Cards>()
            CardScreen(topicId = args.topicId)
        }
        composable<Exam> {
            val args = it.toRoute<Exam>()
            ExamScreen(
                subject = args.subject,
                questionCount = args.questionCount,
                onExamCardClick = { navController.navigate(Question(it)) }
            )
        }
        composable<Question> {
            val args = it.toRoute<Question>()
            ExamQuestions(
                topicId = args.topicId,
                onSubmitExam = { navController.popBackStack() }
            )
        }
        composable<PdfUpload> { PdfUploadScreen(navController) }
        composable<CardGrid> { CardsGridScreen { navController.navigate(Cards(it)) } }
        composable<ExamGrid> {
            ExamGridScreen { topicId ->
                navController.navigate(Exam(subject = topicId, questionCount = 10))
            }
        }
        composable<Profile> {
            ProfileScreen(
                userName = "User 1",
                email = "user1@gmail.com",
                onLogoutPressed = { authViewModel.logout() }
            )
        }
    }
}
