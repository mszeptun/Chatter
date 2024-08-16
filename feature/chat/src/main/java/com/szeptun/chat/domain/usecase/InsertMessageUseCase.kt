package com.szeptun.chat.domain.usecase

import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.repo.ChatRepository
import com.szeptun.common.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertMessageUseCase @Inject constructor(private val repository: ChatRepository) {
    operator fun invoke(message: Message): Flow<Response<Unit>> = flow {
        // Emit Loading state
        emit(Response.Loading())

        // Perform the insertion
        try {
            repository.insertMessage(message)
            // Emit Success state
            emit(Response.Success(Unit))
        } catch (e: Exception) {
            // Emit Error state in case of an exception
            emit(Response.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }
}