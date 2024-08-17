package com.szeptun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.szeptun.database.entity.ChatEntity
import com.szeptun.database.entity.MessageWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert
    suspend fun insertChat(chat: ChatEntity): Long

    @Query("SELECT * FROM chat WHERE id = :chatId")
    suspend fun getChatById(chatId: Long): ChatEntity?

    @Transaction
    @Query("""
        SELECT m.id AS messageId, m.content AS messageContent, m.senderId AS senderId, 
               m.chatId AS chatId, m.timestamp AS messageTimestamp, 
               u.id AS userId, u.name AS userName, u.avatarUrl AS userAvatarUrl
        FROM message m
        LEFT JOIN user u ON m.senderId = u.id
        WHERE m.chatId = :chatId
    """)
    fun getMessagesAndUsersForChat(chatId: Long): Flow<List<MessageWithUser>?>
}