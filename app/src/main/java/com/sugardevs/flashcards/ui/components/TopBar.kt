package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val avatarUrl = authViewModel.avatarUrl

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
                val context = LocalContext.current
                val imageRequest = ImageRequest.Builder(context)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build()

                AsyncImage(
                    model = imageRequest,
                    contentDescription = "User Avatar",
                    placeholder = painterResource(R.drawable.deafult),
                    error = painterResource(R.drawable.deafult),
                    fallback = painterResource(R.drawable.deafult),
                    modifier = Modifier.size(40.dp)
                )

            }
        },
        scrollBehavior = scrollBehavior
    )
}
