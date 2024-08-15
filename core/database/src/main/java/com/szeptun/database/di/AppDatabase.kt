package com.szeptun.database.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.szeptun.database.converter.UserIdsConverter
import com.szeptun.database.dao.ChatDao
import com.szeptun.database.dao.MessageDao
import com.szeptun.database.dao.UserDao
import com.szeptun.database.entity.ChatEntity
import com.szeptun.database.entity.MessageEntity
import com.szeptun.database.entity.UserEntity

@Database(entities = [UserEntity::class, ChatEntity::class, MessageEntity::class], version = 1)
@TypeConverters(UserIdsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}