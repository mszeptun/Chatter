package com.szeptun.chat.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.szeptun.chat.ui.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel) {

    val state by viewModel.uiState.collectAsState()
}