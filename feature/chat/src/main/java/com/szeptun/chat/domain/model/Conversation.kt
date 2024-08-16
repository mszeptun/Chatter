package com.szeptun.chat.domain.model

data class Conversation(
    val chat: Chat,
    val users: List<User>,
    val messages: List<Message>
)