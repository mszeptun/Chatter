package com.szeptun.chat.domain.model

data class Chat(
    val id: Int = 0,
    val userIds: List<Long>
)