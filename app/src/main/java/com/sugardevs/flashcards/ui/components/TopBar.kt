package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sugardevs.flashcards.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    TopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        navigationIcon = if (showBackButton) {
            {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        } else {
            {}
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Image(
                    painter = painterResource(id = R.drawable.pranav),
                    contentDescription = stringResource(R.string.profile),
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
