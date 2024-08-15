package com.szeptun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.szeptun.database.entity.ChatEntity
import com.szeptun.database.entity.ChatWithMessages

@Dao
interface ChatDao {

    @Insert
    suspend fun insertChat(chat: ChatEntity): Long

    @Query("SELECT * FROM chat WHERE id = :chatId")
    suspend fun getChatById(chatId: Long): ChatEntity?

    @Query("SELECT * FROM chat")
    suspend fun getAllChats(): List<ChatEntity>

    @Transaction
    @Query("SELECT * FROM chat WHERE id = :chatId")
    suspend fun getChatWithMessages(chatId: Long): ChatWithMessages?
}