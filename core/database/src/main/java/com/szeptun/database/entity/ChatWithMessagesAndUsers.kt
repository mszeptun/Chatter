package com.szeptun.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ChatWithMessagesAndUsers(
    @Embedded val chat: ChatEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chatId"
    )
    val messages: List<MessageEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ChatEntity::class,
            parentColumn = "id",
            entityColumn = "userIds"
        )
    )
    val users: List<UserEntity>
)