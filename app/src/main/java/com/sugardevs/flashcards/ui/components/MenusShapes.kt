package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FlashCardTopic(
    modifier: Modifier = Modifier,
    subject: String = "Android",
    onCardClick: () -> Unit,
    gradientStartColor: Color = Color(0xFF64B5F6),
    gradientEndColor: Color = Color(0xFF2196F3)
) {
    // Determine text color based on the perceived brightness of the gradient

    val isDarkGradient = (gradientStartColor.red + gradientStartColor.green + gradientStartColor.blue +
            gradientEndColor.red + gradientEndColor.green + gradientEndColor.blue) / 6 < 0.5f
    val textColor = if (isDarkGradient) Color.White else Color.Black

    Card(
        modifier = modifier
            .size(height = 150.dp, width = 150.dp)
            .clickable(
                onClick = { onCardClick() }
            ),
        elevation = CardDefaults.cardElevation(16.dp),

        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(gradientStartColor, gradientEndColor)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = subject,
                    color = textColor,
                    fontSize = 24.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    FlashCardTopic(
        modifier = TODO(),
        subject = TODO(),
        onCardClick = TODO(),
        gradientStartColor = TODO(),
        gradientEndColor = TODO()
    )
}

@Preview
@Composable
fun MenuPreviewDarkGradient() {
    FlashCardTopic(
        subject = "Kotlin",
        gradientStartColor = Color(0xFF7B1FA2),
        gradientEndColor = Color(0xFF4A148C),
        modifier = TODO(),
        onCardClick = TODO()
    )
}

@Preview
@Composable
fun MenuPreviewLightGradient() {
    FlashCardTopic(
        subject = "Compose",
        gradientStartColor = Color(0xFFFFF59D),
        gradientEndColor = Color(0xFFFFFDE7),
        modifier = TODO(),
        onCardClick = TODO()
    )
}