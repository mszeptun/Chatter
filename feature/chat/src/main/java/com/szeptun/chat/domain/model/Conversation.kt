package com.szeptun.chat.domain.model

data class Conversation(
    val chatId: Long,
    val users: List<User>,
    val messages: List<Message>
)