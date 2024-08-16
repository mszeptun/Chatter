package com.szeptun.chat.di

import com.szeptun.chat.data.repo.ChatRepositoryImpl
import com.szeptun.chat.domain.repo.ChatRepository
import com.szeptun.database.dao.ChatDao
import com.szeptun.database.dao.MessageDao
import com.szeptun.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideChatRepository(
        userDao: UserDao,
        messageDao: MessageDao,
        chatDao: ChatDao
    ): ChatRepository =
        ChatRepositoryImpl(userDao = userDao, messageDao = messageDao, chatDao = chatDao)
}