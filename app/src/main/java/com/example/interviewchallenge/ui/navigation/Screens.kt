package com.example.interviewchallenge.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {

    @Serializable
    object Home : Screens

    @Serializable
    object AppList : Screens

    @Serializable
    data class AppDetails(
        val packageName: String
    ) : Screens
}
