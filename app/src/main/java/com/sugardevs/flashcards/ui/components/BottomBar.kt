package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sugardevs.flashcards.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
            .fillMaxWidth(),
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = stringResource(R.string.home)
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.AutoAwesomeMotion,
                        contentDescription = stringResource(R.string.flash_cards)
                    )
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.PictureAsPdf,
                        contentDescription = stringResource(R.string.upload_pdf)
                    )
                }

                    IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.LocalLibrary,
                        contentDescription = stringResource(R.string.library)
                    )
                }

            }
        },
    )
}


@Preview(
    showBackground = true,
)
@Composable
fun BottomBarPreview() {
    BottomBar()
}