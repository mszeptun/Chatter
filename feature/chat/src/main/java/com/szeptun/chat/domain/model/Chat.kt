package com.szeptun.chat.domain.model

data class Chat(
    val id: Long = 0,
    val userIds: List<Long>
)