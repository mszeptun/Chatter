package com.szeptun.chat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.usecase.GetConversationUseCase
import com.szeptun.chat.domain.usecase.InsertMessageUseCase
import com.szeptun.chat.ui.model.MessageType
import com.szeptun.chat.ui.uistate.ChatUiState
import com.szeptun.common.Response
import com.szeptun.common.isLessThanTwentySecondsAgo
import com.szeptun.common.isMoreThanOneHourAgo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getConversationUseCase: GetConversationUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    @Named("chatId") private val chatId: Long
) : ViewModel() {

    //Init app with mocked id
    private var localUserId = 1L
    var text by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow(ChatUiState(localUserId = localUserId))
    val uiState: StateFlow<ChatUiState>
        get() = _uiState

    private lateinit var previousItem: Message

    fun getConversation() {
        getConversationUseCase.invoke(chatId).onEach { response ->
            when (response) {
                is Response.Success -> {
                    val messages = response.data.messages.mapIndexed { index, message ->
                        val isLocalUserMessage = isLocalUserMessage(message.senderId)
                        when {
                            // First item always should be SectionMessage
                            index == 0 -> {
                                MessageType.SectionMessage(
                                    message,
                                    isLocalUserMessage
                                )
                            }

                            message.timestamp.isMoreThanOneHourAgo(previousItem.timestamp) -> {
                                MessageType.SectionMessage(message, isLocalUserMessage)
                            }

                            message.timestamp.isLessThanTwentySecondsAgo(previousItem.timestamp) &&
                                    isTheSameUser(previousItem.senderId, message.senderId) -> {
                                MessageType.SmallSeparationMessage(message, isLocalUserMessage)
                            }

                            else -> {
                                MessageType.NormalMessage(message, isLocalUserMessage)
                            }
                        }.also {
                            previousItem = message
                        }
                    }

                    _uiState.value = uiState.value.copy(
                        isLoading = false,
                        messages = messages,
                        users = response.data.users,
                        isError = false
                    )
                }

                is Response.Loading -> {
                    if (_uiState.value.messages.isEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }

        }.launchIn(viewModelScope)
    }

    fun insertMessage(text: String) {
        val message = Message(
            content = text,
            senderId = localUserId,
            chatId = chatId,
            timestamp = System.currentTimeMillis()
        )
        insertMessageUseCase.invoke(message).launchIn(viewModelScope)
    }

    fun reverseUser() {
        localUserId = _uiState.value.users.first { it.id != localUserId }.id
        _uiState.update { state ->
            state.copy(localUserId = localUserId)
        }
        text = ""
        getConversation()
    }

    fun updateText(newText: String) {
        text = newText
    }

    private fun isLocalUserMessage(id: Long) = id == localUserId

    private fun isTheSameUser(previousMessageSenderId: Long, senderId: Long) =
        previousMessageSenderId == senderId
}