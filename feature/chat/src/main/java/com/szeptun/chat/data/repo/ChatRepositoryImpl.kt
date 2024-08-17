package com.szeptun.chat.data.repo

import com.szeptun.chat.data.mapper.toChatEntity
import com.szeptun.chat.data.mapper.toMessageEntity
import com.szeptun.chat.data.mapper.toUserEntity
import com.szeptun.chat.domain.model.Conversation
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.model.User
import com.szeptun.chat.domain.repo.ChatRepository
import com.szeptun.database.dao.ChatDao
import com.szeptun.database.dao.MessageDao
import com.szeptun.database.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao
) : ChatRepository {

    @Volatile
    private var isDataInitialized = false

    override fun getConversationData(chatId: Long): Flow<Conversation?> = flow {
        // Ensure fake data is inserted if needed
        initializeDataIfNeeded()

        // Now fetch and return Conversation data
        emitAll(chatDao.getMessagesAndUsersForChat(chatId).map { response ->
            if (response == null) {
                null
            } else {
                // Separate lists
                val messages = mutableListOf<Message>()
                val users = mutableSetOf<User>()

                for (result in response) {
                    // Map to Message
                    val message = Message(
                        id = result.messageId,
                        content = result.messageContent,
                        senderId = result.senderId,
                        chatId = result.chatId,
                        timestamp = result.messageTimestamp
                    )
                    messages.add(message)

                    // Map to User, ensure users are unique
                    if (result.userId != null) {
                        val user = User(
                            id = result.userId ?: 0L,
                            name = result.userName ?: "",
                            avatarUrl = result.userAvatarUrl ?: ""
                        )
                        users.add(user)
                    }
                }

                Conversation(
                    chatId = chatId,
                    users = users.toList(),
                    messages = messages.sortedBy { message ->  message.timestamp })
            }
        })
    }

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message.toMessageEntity())
    }

    // Synchronized method to ensure thread-safe initialization
    private suspend fun initializeDataIfNeeded() {
        if (!isDataInitialized) {
            synchronized(this) {
                if (!isDataInitialized) {
                    // Block the current thread to ensure initialization completes
                    runBlocking {
                        insertFakeDataIfNeeded()
                    }
                    isDataInitialized = true
                }
            }
        }
    }

    private suspend fun insertFakeDataIfNeeded() {
        val messageCount = messageDao.getMessageCount()

        //If there is no message then we want to insert fake data
        if (messageCount == 0) {
            FakeDataCreator.createFakeUserData().map {
                it.toUserEntity()
            }.forEach { user ->
                userDao.insertUser(user)
            }

            FakeDataCreator.createFakeChatData().map {
                it.toChatEntity()
            }.forEach { chat ->
                chatDao.insertChat(chat)
            }

            FakeDataCreator.createFakeMessages().map {
                it.toMessageEntity()
            }.forEach { message ->
                messageDao.insertMessage(message)
            }
        }
    }
}