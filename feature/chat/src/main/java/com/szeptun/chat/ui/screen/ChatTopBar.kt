package com.szeptun.chat.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.szeptun.chat.domain.model.User
import com.szeptun.common.theme.ChatterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(modifier: Modifier = Modifier, user: User, onUserReverse: () -> Unit) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        ),
                    model = user.avatarUrl,
                    contentDescription = "User avatar"
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    text = user.name,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.scrim,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                IconButton(onClick = { onUserReverse() }) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reverse",
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* no-op */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    )
}

@Preview
@Composable
fun ChatTopBarPreview() {
    ChatterTheme {
        ChatTopBar(
            user = User(
                name = "Marcin",
                avatarUrl = "https://gravatar.com/avatar/a3b545f49a99e71a75a9bb53740a7b9e?s=400&d=robohash&r=x"
            )
        ) {

        }
    }
}