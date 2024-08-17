package com.szeptun.chat.ui.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.model.User
import com.szeptun.chat.ui.ChatViewModel
import com.szeptun.chat.ui.model.MessageType
import com.szeptun.chat.ui.uistate.ChatUiState
import com.szeptun.common.theme.ChatterTheme

@Composable
fun ChatScreen(viewModel: ChatViewModel) {

    val state by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    Chat(
        state = state,
        text = inputText,
        onInsertMessage = { message -> viewModel.insertMessage(message) },
        onUserReverse = { viewModel.reverseUser() },
        onInputTextChange = { newText ->
            inputText = newText
        }
    )
}

@Composable
fun Chat(
    state: ChatUiState,
    text: TextFieldValue,
    onInsertMessage: (String) -> Unit,
    onUserReverse: () -> Unit,
    onInputTextChange: (TextFieldValue) -> Unit
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                ChatTopBar(user = state.users.first { it.id != state.localUserId }) {
                    onUserReverse()
                }
            }, bottomBar = {
                ChatBox(
                    modifier = Modifier,
                    onSendChatClickListener = { text -> onInsertMessage(text) },
                    text = text,
                    onTextChange = { newText ->
                        onInputTextChange(newText)
                    }
                )
            }) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                reverseLayout = true
            ) {
                items(state.messages) { message ->
                    MessageItem(message = message)

                    HorizontalDivider(
                        thickness = if (message is MessageType.SmallSeparationMessage) 4.dp else 8.dp,
                        color = Color.Transparent
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatPreview() {
    ChatterTheme {
        Chat(
            state = ChatUiState(
                isLoading = false,
                users = listOf(User(name = "Marcin", avatarUrl = "")),
                messages = listOf(
                    MessageType.SmallSeparationMessage(
                        Message(
                            content = "Hello",
                            senderId = 1,
                            timestamp = System.currentTimeMillis(),
                        ),
                        isLocalUserMessage = false
                    ), MessageType.NormalMessage(
                        Message(
                            content = "Hello",
                            senderId = 1,
                            timestamp = System.currentTimeMillis(),
                        ),
                        isLocalUserMessage = true
                    ),
                    MessageType.SectionMessage(
                        Message(
                            content = "Hello",
                            senderId = 1,
                            timestamp = System.currentTimeMillis(),
                        ),
                        isLocalUserMessage = false
                    )
                )
            ),
            text = TextFieldValue(""),
            onInsertMessage = {},
            onUserReverse = { }
        ) {

        }
    }
}