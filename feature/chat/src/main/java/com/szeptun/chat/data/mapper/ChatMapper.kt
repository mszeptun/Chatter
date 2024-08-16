package com.szeptun.chat.data.mapper

import com.szeptun.chat.domain.model.Chat
import com.szeptun.database.entity.ChatEntity

fun ChatEntity.toChat() = Chat(id = id, userIds = userIds)

fun Chat.toChatEntity() = ChatEntity(userIds = userIds)