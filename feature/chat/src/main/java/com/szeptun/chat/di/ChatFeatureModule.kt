package com.szeptun.chat.di

import com.szeptun.chat.ui.navigation.ChatApi
import com.szeptun.chat.ui.navigation.ChatApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ChatFeatureModule {
    //Mock chat id
    @Provides
    @Named("chatId")
    fun provideChatId(): Long = 1

    @Provides
    fun provideChatApi(): ChatApi = ChatApiImpl()
}