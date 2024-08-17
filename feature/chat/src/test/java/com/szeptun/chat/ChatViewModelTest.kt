import app.cash.turbine.test
import com.szeptun.chat.MainCoroutineRule
import com.szeptun.chat.domain.model.Conversation
import com.szeptun.chat.domain.model.Message
import com.szeptun.chat.domain.model.User
import com.szeptun.chat.domain.usecase.GetConversationUseCase
import com.szeptun.chat.domain.usecase.InsertMessageUseCase
import com.szeptun.chat.ui.ChatViewModel
import com.szeptun.chat.ui.model.MessageType
import com.szeptun.chat.ui.uistate.ChatUiState
import com.szeptun.common.ONE_HOUR_MILLIS
import com.szeptun.common.Response
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest : TestWatcher() {

    private val getConversationUseCase: GetConversationUseCase = mockk()
    private val insertMessageUseCase: InsertMessageUseCase = mockk()
    private val chatId = 1L
    private lateinit var viewModel: ChatViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = ChatViewModel(getConversationUseCase, insertMessageUseCase, chatId)
    }

    @Test
    fun `getConversation emits Loading and Success states`() = runTest {
        // Mock the Response
        val mockUsers = listOf(User(1, "User1", "url1"), User(2, "User2", "url2"))
        val mockMessages = listOf(
            Message(1, "Hello", 1L, chatId, System.currentTimeMillis()),
            Message(2, "Hi", 2L, chatId, System.currentTimeMillis())
        )
        val mockResponse = Response.Success(Conversation(chatId, mockUsers, mockMessages))

        coEvery { getConversationUseCase.invoke(chatId) } returns flowOf(mockResponse)

        // Observe the StateFlow
        viewModel.uiState.test {
            //When
            viewModel.getConversation()

            // Then - Loading state
            assertEquals(ChatUiState(isLoading = true, localUserId = 1L), awaitItem())

            // Then - Success state
            val emittedState = awaitItem()
            assertEquals(false, emittedState.isLoading)
            assertEquals(mockUsers, emittedState.users)
            // Check for the number of messages
            assertEquals(2, emittedState.messages.size)
        }
    }

    @Test
    fun `getConversation emits Error state on failure`() = runTest {
        // Mock an Error Response
        val errorResponse = Response.Error<Conversation>("Failed to load conversation")
        coEvery { getConversationUseCase.invoke(chatId) } returns flowOf(errorResponse)

        // Observe the StateFlow
        viewModel.uiState.test {
            //When
            viewModel.getConversation()

            // Then - Loading state
            assertEquals(ChatUiState(isLoading = true, localUserId = 1L), awaitItem())

            // Then - Error state
            val emittedState = awaitItem()
            assertEquals(false, emittedState.isLoading)
            assertEquals(true, emittedState.isError)
        }
    }

    @Test
    fun `when message is first or there was no message for 1 hour then it should be SectionMessage`() =
        runTest {
            // Mock the Response
            val mockUsers = listOf(User(1, "User1", "url1"), User(2, "User2", "url2"))
            val mockMessages = listOf(
                // First message two hours ago
                Message(1, "Hello", 1L, chatId, System.currentTimeMillis() - 2 * ONE_HOUR_MILLIS),
                // Second message more than hour ago from previous one
                Message(2, "Hi", 2L, chatId, System.currentTimeMillis() - ONE_HOUR_MILLIS + 10)
            )
            val mockResponse = Response.Success(Conversation(chatId, mockUsers, mockMessages))

            coEvery { getConversationUseCase.invoke(chatId) } returns flowOf(mockResponse)

            // Observe the StateFlow
            viewModel.uiState.test {
                //When
                viewModel.getConversation()

                // Then - Loading state
                assertEquals(ChatUiState(isLoading = true, localUserId = 1L), awaitItem())

                // Then - Success state
                val emittedState = awaitItem()
                // Check for the number of messages
                assertEquals(
                    2,
                    emittedState.messages.filterIsInstance<MessageType.SectionMessage>().size
                )
            }
        }

    @Test
    fun `when messages are after each less than 20s and same user then it should be SmallSeparationMessage`() =
        runTest {
            // Mock the Response
            val mockUsers = listOf(User(1, "User1", "url1"), User(2, "User2", "url2"))
            //Initial timestamp 15s ago
            val timestamp = System.currentTimeMillis() - 15000L
            val mockMessages = listOf(
                Message(1, "Hello", 1L, chatId, System.currentTimeMillis()),
                // Message after 10 seconds
                Message(3, "Hello", 1L, chatId, timestamp = timestamp + 10000L),
                // Message after 5 seconds
                Message(2, "Hi", 1L, chatId, timestamp = timestamp + 15000L),
            )
            val mockResponse = Response.Success(Conversation(chatId, mockUsers, mockMessages))

            coEvery { getConversationUseCase.invoke(chatId) } returns flowOf(mockResponse)

            // Observe the StateFlow
            viewModel.uiState.test {
                //When
                viewModel.getConversation()

                // Then - Loading state
                assertEquals(ChatUiState(isLoading = true, localUserId = 1L), awaitItem())

                // Then - Success state
                val emittedState = awaitItem()
                // Check for the number of messages
                assertEquals(
                    2,
                    emittedState.messages.filterIsInstance<MessageType.SmallSeparationMessage>().size
                )
            }
        }

    @Test
    fun `insertMessage triggers insertMessageUseCase with correct data`() = runTest {
        // Mock the insert message use case
        coEvery { insertMessageUseCase.invoke(any()) } returns flowOf(Response.Success(Unit))

        val messageText = "New Message"
        viewModel.insertMessage(messageText)

        coVerify {
            insertMessageUseCase.invoke(withArg {
                assertEquals(messageText, it.content)
                assertEquals(chatId, it.chatId)
                // The localUserId
                assertEquals(1L, it.senderId)
            })
        }
    }

    @Test
    fun `updateText changes the text state`() = runTest {
        val newText = "Updated Text"
        viewModel.updateText(newText)
        assertEquals(newText, viewModel.text)
    }
}