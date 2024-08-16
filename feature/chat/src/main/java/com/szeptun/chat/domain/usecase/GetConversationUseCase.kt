package com.szeptun.chat.domain.usecase

import com.szeptun.chat.domain.model.Conversation
import com.szeptun.chat.domain.repo.ChatRepository
import com.szeptun.common.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetConversationUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke(): Flow<Response<Conversation>> =
        repository.getConversationData().map { data ->
            if (data == null) {
                Response.Error("Chat not found or data unavailable")
            } else {
                Response.Success(data)
            }
        }.onStart {
            emit(Response.Loading())
        }.catch { e ->
            emit(Response.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
}