package com.szeptun.chat.data.repo

import com.szeptun.chat.data.mapper.toChat
import com.szeptun.chat.data.mapper.toChatEntity
import com.szeptun.chat.data.mapper.toMessage
import com.szeptun.chat.data.mapper.toMessageEntity
import com.szeptun.chat.data.mapper.toUser
import com.szeptun.chat.data.mapper.toUserEntity
import com.szeptun.chat.domain.model.Conversation
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.repo.ChatRepository
import com.szeptun.database.dao.ChatDao
import com.szeptun.database.dao.MessageDao
import com.szeptun.database.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.LinkedList
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao
) : ChatRepository {

    @Volatile
    private var isDataInitialized = false

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
        val chatData = chatDao.getChatById(0)

        if (chatData == null) {
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

    // Function to insert fake data if needed and then fetch Conversation data
    override fun getConversationData(chatId: Long): Flow<Conversation?> = flow {
        // Ensure fake data is inserted if needed
        initializeDataIfNeeded()

        // Now fetch and return Conversation data
        emitAll(chatDao.getChatWithMessagesAndUsers(chatId).map { response ->
            if (response == null) {
                null
            } else {
                val chat = response.chat.toChat()
                val messages = response.messages.map {
                    it.toMessage()
                }

                val users = response.users.map {
                    it.toUser()
                }

                Conversation(chat = chat, users = users, messages = LinkedList(messages))
            }
        })
    }

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message.toMessageEntity())
    }
}