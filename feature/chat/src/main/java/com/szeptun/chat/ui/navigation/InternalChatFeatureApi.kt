package com.szeptun.chat.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.szeptun.chat.ui.ChatViewModel
import com.szeptun.chat.ui.screen.ChatScreen
import com.szeptun.feature_api.FeatureApi

internal object InternalChatFeatureApi : FeatureApi {

    override fun registerGraph(
        navController: androidx.navigation.NavHostController,
        navGraphBuilder: androidx.navigation.NavGraphBuilder
    ) {
        navGraphBuilder.navigation(
            startDestination = ChatFeature.chatScreenRoute,
            route = ChatFeature.route
        ) {
            composable(ChatFeature.chatScreenRoute) {
                val viewModel = hiltViewModel<ChatViewModel>()
                ChatScreen(viewModel)
            }
        }
    }
}