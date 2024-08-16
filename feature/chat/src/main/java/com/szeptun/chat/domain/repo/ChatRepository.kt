package com.szeptun.chat.domain.repo

import com.szeptun.chat.domain.model.Conversation
import com.szeptun.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getConversationData(chatId: Long = 0): Flow<Conversation?>

    suspend fun insertMessage(message: Message)
}