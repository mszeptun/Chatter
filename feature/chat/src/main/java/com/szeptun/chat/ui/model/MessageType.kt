package com.szeptun.chat.ui.model

import com.szeptun.chat.domain.model.Message

sealed class MessageType(open val message: Message, open val isLocalUserMessage: Boolean) {
    data class NormalMessage(
        override val message: Message,
        override val isLocalUserMessage: Boolean
    ) : MessageType(message, isLocalUserMessage)

    data class SmallSeparationMessage(
        override val message: Message,
        override val isLocalUserMessage: Boolean
    ) : MessageType(message, isLocalUserMessage)

    data class SectionMessage(
        override val message: Message,
        override val isLocalUserMessage: Boolean
    ) : MessageType(message, isLocalUserMessage)
}