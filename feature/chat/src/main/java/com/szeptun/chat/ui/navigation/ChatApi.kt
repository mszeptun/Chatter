package com.szeptun.chat.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.szeptun.feature_api.FeatureApi

interface ChatApi : FeatureApi

class ChatApiImpl : ChatApi {
    override fun registerGraph(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
        InternalChatFeatureApi.registerGraph(navController, navGraphBuilder)
    }
}