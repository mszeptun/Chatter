package com.szeptun.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.szeptun.database.converter.UserIdsConverter

@Entity("chat")
@TypeConverters(UserIdsConverter::class)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userIds: List<Long>
)