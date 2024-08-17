package com.szeptun.chat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.ui.model.MessageType
import com.szeptun.common.theme.ChatterTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageItem(message: MessageType) {
    when (message) {
        is MessageType.NormalMessage, is MessageType.SmallSeparationMessage -> MessageContent(
            message = message
        )

        is MessageType.SectionMessage -> SectionMessage(message = message)
    }
}

@Composable
fun MessageContent(modifier: Modifier = Modifier, message: MessageType) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (message.isLocalUserMessage) 32.dp else 12.dp,
                end = if (message.isLocalUserMessage) 12.dp else 32.dp
            ),
        horizontalArrangement = if (message.isLocalUserMessage) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .messageBoxModifier(message)
                .background(if (message.isLocalUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
        ) {
            Text(
                color = if (message.isLocalUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                text = message.message.content
            )
        }
    }
}

@Composable
fun SectionMessage(message: MessageType) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            text = message.message.timestamp.formatTimestamp(),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
        )
        MessageContent(modifier = Modifier.padding(top = 4.dp), message = message)
    }
}

private fun Modifier.messageBoxModifier(message: MessageType) = this
    .clip(
        RoundedCornerShape(
            topStart = 48f,
            topEnd = 48f,
            bottomStart = if (message.isLocalUserMessage) 48f else 0f,
            bottomEnd = if (message.isLocalUserMessage) 0f else 48f,
        )
    )

private fun Long.formatTimestamp(): String {
    val sdf = SimpleDateFormat("EEEE HH:mm", Locale.getDefault())
    val date = Date(this)
    return sdf.format(date)
}

@Preview
@Composable
fun MessageItemPreview() {
    ChatterTheme {
        Column {
            MessageItem(
                message = MessageType.SectionMessage(
                    Message(
                        content = "Hello",
                        senderId = 1,
                        timestamp = System.currentTimeMillis(),
                    ),
                    isLocalUserMessage = true
                )
            )

            MessageItem(
                message = MessageType.SectionMessage(
                    Message(
                        content = "Hello",
                        senderId = 1,
                        timestamp = System.currentTimeMillis(),
                    ),
                    isLocalUserMessage = false
                )
            )

            MessageItem(
                message = MessageType.NormalMessage(
                    Message(
                        content = "Hello",
                        senderId = 1,
                        timestamp = System.currentTimeMillis(),
                    ),
                    isLocalUserMessage = true
                )
            )

            MessageItem(
                message = MessageType.NormalMessage(
                    Message(
                        content = "Hello",
                        senderId = 1,
                        timestamp = System.currentTimeMillis(),
                    ),
                    isLocalUserMessage = false
                )
            )

            MessageItem(
                message = MessageType.SmallSeparationMessage(
                    Message(
                        content = "Hello",
                        senderId = 1,
                        timestamp = System.currentTimeMillis(),
                    ),
                    isLocalUserMessage = true
                )
            )
            MessageItem(
                message = MessageType.SmallSeparationMessage(
                    Message(
                        content = "Hello",
                        senderId = 1,
                        timestamp = System.currentTimeMillis(),
                    ),
                    isLocalUserMessage = true
                )
            )
        }
    }
}