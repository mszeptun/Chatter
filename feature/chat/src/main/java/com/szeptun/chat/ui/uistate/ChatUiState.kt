package com.szeptun.chat.ui.uistate

import com.szeptun.chat.domain.model.User
import com.szeptun.chat.ui.model.MessageType

data class ChatUiState(
    val isLoading: Boolean = true,
    val users: List<User> = emptyList(),
    val messages: List<MessageType> = emptyList(),
    val isError: Boolean = false
)