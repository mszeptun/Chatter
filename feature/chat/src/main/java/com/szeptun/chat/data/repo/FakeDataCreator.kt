package com.szeptun.chat.data.repo

import com.szeptun.chat.domain.model.Chat
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.model.User
import kotlin.random.Random

object FakeDataCreator {
    // Creating fake Users, assuming local user id will be 0
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
            userIds = listOf(0, 1)
        )
    )

    // Create fake chat messages for previously created users and chat
    fun createFakeMessages(
        localUserId: Long = 1,
        interlocutorId: Long = 2,
        chatId: Long = 1
    ): List<Message> {
        val messages = mutableListOf<Message>()
        val seed = 42L // Use a fixed seed for deterministic results
        val random = Random(seed)
        val startTime = System.currentTimeMillis() - 3 * 60 * 60 * 1000 // Start 3 hours ago
        var lastTimestamp = startTime

        val conversation = listOf(
            Pair(localUserId, "Hey, what's up?"),
            Pair(interlocutorId, "Not much, just chilling. You?"),
            Pair(localUserId, "Same here. Wanna grab a coffee later?"),
            Pair(interlocutorId, "Sure! What time?"),
            Pair(localUserId, "How about 3 PM?"),
            Pair(interlocutorId, "Perfect. See you then!"),
            Pair(localUserId, "Cool, see you soon!"),
            Pair(interlocutorId, "By the way, did you finish that project?"),
            Pair(localUserId, "Almost. Just need to wrap up a few things."),
            Pair(interlocutorId, "Nice! Let me know if you need any help."),
            Pair(localUserId, "Thanks, appreciate it!"),
            Pair(interlocutorId, "No problem!"),
            Pair(localUserId, "Hey, I'm on my way!"),
            Pair(interlocutorId, "Great, see you in a bit!"),
            Pair(localUserId, "I'm here. Where are you?"),
            Pair(interlocutorId, "Just parking, be there in 2 mins."),
            Pair(localUserId, "Got it, waiting inside."),
            Pair(interlocutorId, "Sorry, ran into some traffic."),
            Pair(localUserId, "No worries, take your time."),
            Pair(interlocutorId, "Almost there!"),
            Pair(interlocutorId, "I see you, waving at you!"),
            Pair(interlocutorId, "Haha, found you!"),
            Pair(localUserId, "Let's grab that coffee."),
            Pair(interlocutorId, "Absolutely. Let's go!"),
            Pair(localUserId, "Nice catching up with you."),
            Pair(interlocutorId, "Same here! We should do this more often."),
            Pair(localUserId, "For sure. Let's plan something for next weekend."),
            Pair(interlocutorId, "Sounds good! I'll check my schedule."),
            Pair(localUserId, "Cool. Talk to you later!"),
            Pair(interlocutorId, "Bye!")
        )

        for ((index, message) in conversation.withIndex()) {
            val (senderId, content) = message

            // Randomly decide the time offset based on conversation context
            val timeOffset = when (random.nextInt(3)) {
                0 -> random.nextInt(10_000) + 5_000L  // within 5 to 15 seconds
                1 -> random.nextInt(20_000) + 15_000L // within 15 to 35 seconds
                else -> random.nextInt(300_000) + 3_600_000L // more than 1 hour later
            }

            // Ensure some messages are sent in quick succession
            lastTimestamp += if (index % 5 == 0) 5_000L else timeOffset

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