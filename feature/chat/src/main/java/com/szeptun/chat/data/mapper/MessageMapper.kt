package com.szeptun.chat.data.mapper

import com.szeptun.chat.domain.model.Message
import com.szeptun.database.entity.MessageEntity

fun MessageEntity.toMessage() = Message(
    id = id,
    content = content,
    senderId = senderId,
    chatId = chatId,
    timestamp = timestamp
)

fun Message.toMessageEntity() = MessageEntity(
    content = content,
    senderId = senderId,
    chatId = chatId,
    timestamp = timestamp
)