package com.szeptun.database.entity

data class MessageWithUser(
    val messageId: Long,
    val messageContent: String,
    val senderId: Long,
    val chatId: Long,
    val messageTimestamp: Long,
    val userId: Long?,
    val userName: String?,
    val userAvatarUrl: String?
)