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
        user1Id: Long = 0,
        user2Id: Long = 1,
        chatId: Long = 0
    ): List<Message> {
        val messages = mutableListOf<Message>()
        val seed = 42L // Use a fixed seed for deterministic results
        val random = Random(seed)
        val startTime = System.currentTimeMillis() - 3 * 60 * 60 * 1000 // Start 3 hours ago
        var lastTimestamp = startTime

        val conversation = listOf(
            Pair(user1Id, "Hey, what's up?"),
            Pair(user2Id, "Not much, just chilling. You?"),
            Pair(user1Id, "Same here. Wanna grab a coffee later?"),
            Pair(user2Id, "Sure! What time?"),
            Pair(user1Id, "How about 3 PM?"),
            Pair(user2Id, "Perfect. See you then!"),
            Pair(user1Id, "Cool, see you soon!"),
            Pair(user2Id, "By the way, did you finish that project?"),
            Pair(user1Id, "Almost. Just need to wrap up a few things."),
            Pair(user2Id, "Nice! Let me know if you need any help."),
            Pair(user1Id, "Thanks, appreciate it!"),
            Pair(user2Id, "No problem!"),
            Pair(user1Id, "Hey, I'm on my way!"),
            Pair(user2Id, "Great, see you in a bit!"),
            Pair(user1Id, "I'm here. Where are you?"),
            Pair(user2Id, "Just parking, be there in 2 mins."),
            Pair(user1Id, "Got it, waiting inside."),
            Pair(user2Id, "Sorry, ran into some traffic."),
            Pair(user1Id, "No worries, take your time."),
            Pair(user2Id, "Almost there!"),
            Pair(user1Id, "I see you, waving at you!"),
            Pair(user2Id, "Haha, found you!"),
            Pair(user1Id, "Let's grab that coffee."),
            Pair(user2Id, "Absolutely. Let's go!"),
            Pair(user1Id, "Nice catching up with you."),
            Pair(user2Id, "Same here! We should do this more often."),
            Pair(user1Id, "For sure. Let's plan something for next weekend."),
            Pair(user2Id, "Sounds good! I'll check my schedule."),
            Pair(user1Id, "Cool. Talk to you later!"),
            Pair(user2Id, "Bye!")
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