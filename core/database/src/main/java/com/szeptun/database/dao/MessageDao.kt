package com.szeptun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.szeptun.database.entity.MessageEntity


@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("SELECT * FROM message WHERE id = :messageId")
    suspend fun getMessageById(messageId: Long): MessageEntity?

    @Query("SELECT COUNT(*) FROM message")
    suspend fun getMessageCount(): Int
}