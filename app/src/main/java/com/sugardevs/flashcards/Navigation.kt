package com.sugardevs.flashcards

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sugardevs.flashcards.ui.components.BottomBar
import com.sugardevs.flashcards.ui.components.ExamQuestions
import com.sugardevs.flashcards.ui.components.TopBar
import com.sugardevs.flashcards.ui.screens.CardScreen
import com.sugardevs.flashcards.ui.screens.ExamScreen
import com.sugardevs.flashcards.ui.screens.HomeScreen
import com.sugardevs.flashcards.ui.screens.PdfUploadScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Cards

@Serializable
object Exam

@Serializable
object PdfUpload

@Serializable
object Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topBarTitle = when (currentDestination?.route) {
        Home.javaClass.name -> "Home"
        Cards.javaClass.name -> "Flash Cards"
        Exam.javaClass.name -> "Exam"
        Question.javaClass.name -> "Question"
        PdfUpload.javaClass.name -> "Upload PDF"
        else -> "Flashcards App"
    }

    Scaffold(
        topBar = {
            if (currentDestination?.route != null) {
                TopBar(
                    title = topBarTitle,
                    showBackButton = navController.previousBackStackEntry != null,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier,
                    onProfileClick = {}
                )
            }
        },
        bottomBar = {
            val isTopLevel = currentDestination?.route == Home.javaClass.name ||
                    currentDestination?.route == Cards.javaClass.name ||
                    currentDestination?.route == PdfUpload.javaClass.name

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
                        navController.navigate(Cards) {
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
                        navController.navigate(Exam) {
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Slide in from the right
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // Slide out to the left
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // Slide in from the left (for pop)
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // Slide out to the right (for pop)
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        composable<Home> {
            HomeScreen()
        }
        composable<Cards> {
            CardScreen()
        }
        composable<Exam> {
            ExamScreen()
        }
        composable<Question> {
            ExamQuestions()
        }
        composable<PdfUpload> {
            PdfUploadScreen()
        }
    }
}