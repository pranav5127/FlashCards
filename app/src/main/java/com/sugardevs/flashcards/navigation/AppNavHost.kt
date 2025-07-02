package com.sugardevs.flashcards.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sugardevs.flashcards.ui.components.ExamQuestions
import com.sugardevs.flashcards.ui.screens.CardScreen
import com.sugardevs.flashcards.ui.screens.CardsGridScreen
import com.sugardevs.flashcards.ui.screens.ExamGridScreen
import com.sugardevs.flashcards.ui.screens.ExamScreen
import com.sugardevs.flashcards.ui.screens.HomeScreen
import com.sugardevs.flashcards.ui.screens.PdfUploadScreen
import com.sugardevs.flashcards.ui.screens.ProfileScreen
import com.sugardevs.flashcards.ui.screens.auth.ForgotPasswordScreen
import com.sugardevs.flashcards.ui.screens.auth.SignInScreen
import com.sugardevs.flashcards.ui.screens.auth.SignUpScreen
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    // Screen order map
    val screenOrder = remember {
        mapOf(
            SignIn::class.qualifiedName!! to 0,
            SignUp::class.qualifiedName!! to 0,
            PasswordReset::class.qualifiedName!! to 0,
            Home::class.qualifiedName!! to 1,
            CardGrid::class.qualifiedName!! to 2,
            Cards::class.qualifiedName!! to 3,
            Exam::class.qualifiedName!! to 4,
            Question::class.qualifiedName!! to 5,
            PdfUpload::class.qualifiedName!! to 2,
            ExamGrid::class.qualifiedName!! to 2,
            Profile::class.qualifiedName!! to 10
        )
    }


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            val newOrder = screenOrder[initialState.destination.route] ?: 0
            val targetOrder = screenOrder[targetState.destination.route] ?: 0

            if (targetOrder > newOrder) {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(
                    tween(300)
                )
            } else {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) + fadeIn(
                    tween(300)
                )
            }
        },
        exitTransition = {
            val newOrder = screenOrder[initialState.destination.route] ?: 0
            val targetOrder = screenOrder[targetState.destination.route] ?: 0

            if (targetOrder > newOrder) {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(
                    tween(300)
                )
            } else {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) + fadeOut(
                    tween(300)
                )
            }
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
                onNavigateToSignUp = { navController.navigate(SignUp) },
                onNavigationToForgetPasswordReset = { navController.navigate(PasswordReset)}
            )
        }
        composable<PasswordReset> {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack()}
            )
        }

        composable<Home> {
            HomeScreen(navController = navController, userName = authViewModel.userDisplayName.value)
        }

        composable<Cards> {
            val args = it.toRoute<Cards>()
            CardScreen(topicId = args.topicId)
        }
        composable<Exam> { it ->
            val args = it.toRoute<Exam>()
            ExamScreen(
                subject = args.subject,
                onExamCardClick = { navController.navigate(Question(it)) },
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
                navController.navigate(Exam(subject = topicId))
            }
        }
        composable<Profile> {
            ProfileScreen(
                userName = authViewModel.userDisplayName.value,
                email = authViewModel.userName,
                onLogoutPressed = {
                    authViewModel.logout {
                        navController.navigate(SignIn) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

    }
}
