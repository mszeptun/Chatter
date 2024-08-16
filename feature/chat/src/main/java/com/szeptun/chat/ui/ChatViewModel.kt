package com.szeptun.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.usecase.GetConversationUseCase
import com.szeptun.chat.domain.usecase.InsertMessageUseCase
import com.szeptun.chat.ui.model.MessageType
import com.szeptun.chat.ui.uistate.ChatUiState
import com.szeptun.common.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getConversationUseCase: GetConversationUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    @Named("localUserId") private val localUserId: Long,
    @Named("chatId") private val chatId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState>
        get() = _uiState

    private lateinit var previousItem: Message

    init {
        getConversation()
    }

    private fun getConversation() {
        getConversationUseCase.invoke(chatId).onEach { response ->
            when (response) {
                is Response.Success -> {
                    val messages = response.data.messages.map { message ->
                        val isLocalUserMessage = isLocalUserMessage(message.senderId)
                        when {
                            // Previous item is not initialized so it has to be first message
                            !::previousItem.isInitialized -> {
                                MessageType.SectionMessage(
                                    message,
                                    isLocalUserMessage
                                )
                            }

                            isMoreThanOneHourAgo(message.timestamp) -> {
                                MessageType.SectionMessage(message, isLocalUserMessage)
                            }

                            isLessThanTwentySecondsAgo(message.timestamp) &&
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

    fun insertMessage(message: Message) {
        insertMessageUseCase.invoke(message).launchIn(viewModelScope)
    }

    private fun isMoreThanOneHourAgo(timestamp: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()

        return currentTimeMillis - timestamp > ONE_HOUR_MILLIS
    }

    private fun isLocalUserMessage(id: Long) = id == localUserId

    private fun isLessThanTwentySecondsAgo(timestamp: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()

        return currentTimeMillis - timestamp < TWENTY_SECONDS_MILLIS
    }

    private fun isTheSameUser(previousMessageSenderId: Long, senderId: Long) =
        previousMessageSenderId == senderId

    companion object {
        private const val ONE_HOUR_MILLIS = 60 * 60 * 1000
        private const val TWENTY_SECONDS_MILLIS = 20 * 1000
    }
}