package com.szeptun.chat.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.szeptun.chat.R
import com.szeptun.common.theme.ChatterTheme

@Composable
fun ChatBox(
    onSendChatClickListener: (String) -> Unit,
    modifier: Modifier = Modifier,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit
) {
    Row(modifier = modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { newText ->
                onTextChange(newText)
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .border(1.dp, color = MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.scrim,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                errorContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            ),
            placeholder = {
                Text(text = stringResource(id = R.string.text_box_placeholder))
            }
        )
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.CenterVertically),
            enabled = text.text.isNotBlank(),
            onClick = {
                onSendChatClickListener(text.text)
                onTextChange(TextFieldValue(""))
            },
            colors = IconButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send icon",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun ChatBoxPreview() {
    ChatterTheme {
        Column {
            ChatBox(
                onSendChatClickListener = {},
                modifier = Modifier,
                text = TextFieldValue("Hello"),
                {})
            ChatBox(
                onSendChatClickListener = {},
                modifier = Modifier,
                text = TextFieldValue(""),
                {})
        }
    }
}