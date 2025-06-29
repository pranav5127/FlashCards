package com.sugardevs.flashcards.navigation

import kotlinx.serialization.Serializable

@Serializable
object SignUp

@Serializable
object SignIn

@Serializable
object PasswordReset

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