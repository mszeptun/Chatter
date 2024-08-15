package com.szeptun.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("avatarUrl")
    val avatarUrl: String
)