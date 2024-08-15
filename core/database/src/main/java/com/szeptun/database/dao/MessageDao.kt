package com.szeptun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.szeptun.database.entity.MessageEntity


@Dao
interface MessageDao {

    @Insert
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("SELECT * FROM message WHERE id = :messageId")
    suspend fun getMessageById(messageId: Long): MessageEntity?

    @Query("SELECT * FROM message WHERE chatId = :chatId")
    suspend fun getMessagesForChat(chatId: Long): List<MessageEntity>

    @Query("SELECT * FROM message WHERE senderId = :senderId")
    suspend fun getMessagesForUser(senderId: Long): List<MessageEntity>
}