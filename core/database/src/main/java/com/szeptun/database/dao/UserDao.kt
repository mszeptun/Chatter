package com.szeptun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.szeptun.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)
}