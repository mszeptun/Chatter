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

    private val _uiState = MutableStateFlow(ChatUiState(localUserId = localUserId))
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

                            isMoreThanOneHourAgo(previousItem.timestamp, message.timestamp) -> {
                                MessageType.SectionMessage(message, isLocalUserMessage)
                            }

                            isLessThanTwentySecondsAgo(previousItem.timestamp, message.timestamp) &&
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
                        // Reverse the list so reverseLayout = true flag in LazyColumn will work correctly
                        messages = messages.reversed(),
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
        getConversation()
    }

    private fun isMoreThanOneHourAgo(previousMessageTimestamp: Long, timestamp: Long): Boolean =
        timestamp - previousMessageTimestamp > ONE_HOUR_MILLIS

    private fun isLocalUserMessage(id: Long) = id == localUserId

    private fun isLessThanTwentySecondsAgo(
        previousMessageTimestamp: Long,
        timestamp: Long
    ): Boolean = timestamp - previousMessageTimestamp < TWENTY_SECONDS_MILLIS

    private fun isTheSameUser(previousMessageSenderId: Long, senderId: Long) =
        previousMessageSenderId == senderId

    companion object {
        private const val ONE_HOUR_MILLIS = 60 * 60 * 1000
        private const val TWENTY_SECONDS_MILLIS = 20 * 1000
    }
}