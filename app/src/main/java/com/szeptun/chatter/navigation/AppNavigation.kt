package com.szeptun.chatter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.szeptun.chat.ui.navigation.ChatFeature

@Composable
fun AppNavGraph(navController: NavHostController, navigationProvider: NavigationProvider) {
    NavHost(navController = navController, startDestination = ChatFeature.route) {
        navigationProvider.chatApi.registerGraph(navController, this)
    }
}