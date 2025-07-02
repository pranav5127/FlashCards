package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sugardevs.flashcards.R
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onFlashCardsClick: () -> Unit = {},
    onUploadPdfClick: () -> Unit = {},
    onExamClick: () -> Unit = {}
) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BottomBarItem(
                    icon = Icons.Filled.Home,
                    label = stringResource(R.string.home),
                    onClick = onHomeClick
                )
                BottomBarItem(
                    icon = Icons.Filled.AutoAwesomeMotion,
                    label = stringResource(R.string.flash_cards),
                    onClick = onFlashCardsClick
                )
                BottomBarItem(
                    icon = Icons.Filled.PictureAsPdf,
                    label = stringResource(R.string.upload_pdf),
                    onClick = onUploadPdfClick
                )
                BottomBarItem(
                    icon = Icons.Filled.LocalLibrary,
                    label = stringResource(R.string.exam),
                    onClick = onExamClick
                )
            }
        }
    )
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label
        )
        Text(
            text = label,
            fontSize = 10.sp
        )
    }
}