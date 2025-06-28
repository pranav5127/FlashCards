package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sugardevs.flashcards.R

@Composable
fun ProfileScreen(
    userName: String,
    email: String,
    onLogoutPressed: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.pranav),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
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
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.onPrimary
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
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = email,
                color = MaterialTheme.colorScheme.onPrimary

            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onLogoutPressed
        ) {
            Text(stringResource(R.string.log_out))
        }

    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        userName = "User1",
        email = "user1@email.com",
        onLogoutPressed = { }
    )
}