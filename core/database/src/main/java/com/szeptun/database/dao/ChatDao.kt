package com.szeptun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.szeptun.database.entity.ChatEntity
import com.szeptun.database.entity.ChatWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert
    suspend fun insertChat(chat: ChatEntity): Long

    @Query("SELECT * FROM chat WHERE id = :chatId")
    suspend fun getChatById(chatId: Long): ChatEntity?

    @Transaction
    @Query("SELECT * FROM chat WHERE id = :chatId")
    fun getChatWithMessages(chatId: Long): Flow<ChatWithMessages?>
}