package com.example.interviewchallenge.ui.ui_utils

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.toArgb
import com.example.interviewchallenge.ui.theme.AccentPurpleColor
import com.example.interviewchallenge.ui.theme.BackgroundColor

@Composable
fun ChangeStatusBarAndNavigationBarColor(isDarkMode: Boolean, context: ComponentActivity) {


    val statusColor = MaterialTheme.colorScheme.AccentPurpleColor.toArgb()
    val navBarColor = MaterialTheme.colorScheme.BackgroundColor.toArgb()

    val statusBarStyle = if (isDarkMode) {
        SystemBarStyle.dark(
            statusColor,
        )
    } else {
        SystemBarStyle.light(
            statusColor,
            statusColor
        )
    }

    DisposableEffect(isDarkMode) {
        context.enableEdgeToEdge(
            statusBarStyle = statusBarStyle,
            navigationBarStyle = if (!isDarkMode) {
                SystemBarStyle.light(
                    navBarColor,
                    navBarColor
                )
            } else {
                SystemBarStyle.dark(
                    navBarColor
                )
            }
        )
        onDispose { }
    }
}