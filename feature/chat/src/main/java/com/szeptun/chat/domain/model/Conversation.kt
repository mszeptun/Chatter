package com.szeptun.chat.domain.model

import java.util.LinkedList

data class Conversation(
    val chat: Chat,
    val users: List<User>,
    val messages: LinkedList<Message>
)