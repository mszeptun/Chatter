package com.szeptun.chat.data.mapper

import com.szeptun.chat.domain.model.User
import com.szeptun.database.entity.UserEntity

fun UserEntity.toUser() = User(id = id, name = name, avatarUrl = avatarUrl)

fun User.toUserEntity() = UserEntity(name = name, avatarUrl = avatarUrl)