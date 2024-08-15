package com.szeptun.database.di

import android.content.Context
import androidx.room.Room
import com.szeptun.database.dao.ChatDao
import com.szeptun.database.dao.MessageDao
import com.szeptun.database.dao.UserDao
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideChatDao(db: AppDatabase): ChatDao {
        return db.chatDao()
    }

    @Provides
    fun provideMessageDao(db: AppDatabase): MessageDao {
        return db.messageDao()
    }
}