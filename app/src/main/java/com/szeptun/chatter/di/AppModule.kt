package com.szeptun.chatter.di

import com.szeptun.chat.ui.navigation.ChatApi
import com.szeptun.chatter.navigation.NavigationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideNavigationProvider(chatApi: ChatApi): NavigationProvider =
        NavigationProvider(chatApi)
}