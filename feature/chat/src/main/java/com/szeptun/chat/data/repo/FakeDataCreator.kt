package com.szeptun.chat.data.repo

import com.szeptun.chat.domain.model.Chat
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.model.User

object FakeDataCreator {
    // Creating fake Users, assuming local user id will be 1
    fun createFakeUserData() = listOf(
        User(
            name = "Marcin",
            avatarUrl = "https://gravatar.com/avatar/bcf4a51fd3d56c9fdc2f91daa68630ce?s=400&d=robohash&r=x"
        ),
        User(
            name = "Samuel",
            avatarUrl = "https://gravatar.com/avatar/a3b545f49a99e71a75a9bb53740a7b9e?s=400&d=robohash&r=x"
        )
    )

    // Creating fake Chat with previously created users
    fun createFakeChatData() = listOf(
        Chat(
            userIds = listOf(1, 2)
        )
    )

    fun createFakeMessages(
        localUserId: Long = 1,
        interlocutorId: Long = 2,
        chatId: Long = 1
    ): List<Message> {
        val messages = mutableListOf<Message>()
        // Start 3 hours ago
        val startTime = System.currentTimeMillis() - 3 * 60 * 60 * 1000
        var lastTimestamp = startTime
        val oneHourMillis = 60 * 60 * 1000
        val twentySecondsMillis = 20 * 1000

        val conversation = listOf(
            Triple(localUserId, "Hey, what's up?", twentySecondsMillis),
            Triple(interlocutorId, "Not much, just chilling. You?", twentySecondsMillis),
            Triple(localUserId, "Same here. Wanna grab a coffee later?", twentySecondsMillis),
            Triple(interlocutorId, "Sure! What time?", oneHourMillis + 60 * 1000),
            Triple(localUserId, "How about 3 PM?", twentySecondsMillis),
            Triple(interlocutorId, "Perfect. See you then!", twentySecondsMillis),
            Triple(localUserId, "Cool, see you soon!", twentySecondsMillis),
            Triple(
                interlocutorId,
                "By the way, did you finish that project?",
                twentySecondsMillis
            ),
            Triple(
                localUserId,
                "Almost. Just need to wrap up a few things.",
                twentySecondsMillis
            ),
            Triple(
                interlocutorId,
                "Nice! Let me know if you need any help.",
                twentySecondsMillis
            ),
            Triple(localUserId, "Thanks, appreciate it!", twentySecondsMillis),
            Triple(interlocutorId, "No problem!", twentySecondsMillis),
            Triple(localUserId, "Hey, I'm on my way!", oneHourMillis + 60 * 1000),
            Triple(interlocutorId, "Great, see you in a bit!", twentySecondsMillis),
            Triple(localUserId, "I'm here. Where are you?", twentySecondsMillis),
            Triple(interlocutorId, "Just parking, be there in 2 mins.", twentySecondsMillis),
            Triple(localUserId, "Got it, waiting inside.", twentySecondsMillis),
            Triple(interlocutorId, "Sorry, ran into some traffic.", twentySecondsMillis),
            Triple(localUserId, "No worries, take your time.", twentySecondsMillis),
            Triple(interlocutorId, "Almost there!", twentySecondsMillis),
            Triple(interlocutorId, "I see you, waving at you!", twentySecondsMillis - 1),
            Triple(interlocutorId, "Haha, found you!", twentySecondsMillis - 1),
            Triple(localUserId, "Let's grab that coffee.", twentySecondsMillis),
            Triple(interlocutorId, "Absolutely. Let's go!", twentySecondsMillis),
            Triple(localUserId, "Nice catching up with you.", twentySecondsMillis),
            Triple(
                interlocutorId,
                "Same here! We should do this more often.",
                twentySecondsMillis
            ),
            Triple(
                localUserId,
                "For sure. Let's plan something for next weekend.",
                twentySecondsMillis
            ),
            Triple(interlocutorId, "Sounds good! I'll check my schedule.", twentySecondsMillis),
            Triple(localUserId, "Cool. Talk to you later!", twentySecondsMillis),
            Triple(interlocutorId, "Bye!", twentySecondsMillis)
        )

        for (message in conversation) {
            val (senderId, content, timeDifference) = message

            lastTimestamp += timeDifference

            val messageEntity = Message(
                content = content,
                senderId = senderId,
                chatId = chatId,
                timestamp = lastTimestamp
            )

            messages.add(messageEntity)
        }

        return messages
    }
}