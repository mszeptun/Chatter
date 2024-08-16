package com.szeptun.chat.domain.model

data class User(
    val id: Long = 0L,
    val name: String,
    val avatarUrl: String
)