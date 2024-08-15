package com.szeptun.database.converter

import androidx.room.TypeConverter

class UserIdsConverter {
    @TypeConverter
    fun fromUserIds(userIds: List<Long>): String {
        return userIds.joinToString(separator = ",")
    }

    @TypeConverter
    fun toUserIds(data: String): List<Long> {
        return data.split(",").map { it.toLong() }
    }
}