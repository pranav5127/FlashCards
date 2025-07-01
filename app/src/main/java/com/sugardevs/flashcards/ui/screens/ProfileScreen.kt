package com.sugardevs.flashcards.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.viewModels.auth.AuthViewModel

private const val TAG = "ProfileScreen"

@Composable
fun ProfileScreen(
    userName: String,
    email: String,
    onLogoutPressed: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    val context = LocalContext.current
    val avatarUrl by authViewModel.avatarUrl.collectAsState()

    Log.d(TAG, "Composing ProfileScreen")
    Log.d(TAG, "Avatar URL from ViewModel: $avatarUrl")

    val imageRequest = ImageRequest.Builder(context)
        .data(avatarUrl)
        .crossfade(true)
        .build()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Log.d(TAG, "Rendering AsyncImage")
        AsyncImage(
            model = imageRequest,
            contentDescription = "User Avatar",
            placeholder = painterResource(R.drawable.deafult),
            error = painterResource(R.drawable.deafult),
            fallback = painterResource(R.drawable.deafult),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),

            )



        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.user_name),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.account),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = email,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            Log.d(TAG, "Logout button clicked")
            onLogoutPressed()
        }) {
            Text(stringResource(R.string.log_out))
        }
    }
}
