package com.szeptun.chat.data.repo

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao
) : ChatRepository {

    init {
        runBlocking {
            insertFakeDataIfNeeded()
        }
    }

    override fun getConversationData(chatId: Long): Flow<Conversation?> =
        chatDao.getChatWithMessages(chatId).map { response ->
            if (response == null) {
                null
            } else {
                val messagesSorted = response.messages.map { messageEntity ->
                    messageEntity.toMessage()
                }.sortedBy { message ->
                    message.timestamp
                }

                val users = response.chat.userIds.map { userId ->
                    userDao.getUserById(userId)
                }.mapNotNull { userEntity ->
                    userEntity?.toUser()
                }

                Conversation(
                    chatId = chatId,
                    users = users,
                    messages = messagesSorted
                )
            }
        }

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message.toMessageEntity())
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