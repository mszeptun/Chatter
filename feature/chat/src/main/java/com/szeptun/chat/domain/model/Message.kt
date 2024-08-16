package com.szeptun.chat.domain.model

data class Message(
    val id: Long = 0L,
    val content: String,
    val senderId: Long,
    val chatId: Long = 0L,
    val timestamp: Long
)