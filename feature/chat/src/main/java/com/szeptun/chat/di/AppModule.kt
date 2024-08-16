package com.szeptun.chat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object LocalUserModule {
    //Mock local user id as 0
    @Provides
    @Named("localUserId")
    fun provideLocalUser(): Long = 0
}