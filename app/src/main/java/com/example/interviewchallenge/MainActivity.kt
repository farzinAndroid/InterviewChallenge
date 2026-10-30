package com.example.interviewchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.interviewchallenge.ui.navigation.NavGraph
import com.example.interviewchallenge.ui.theme.InterviewChallengeTheme
import com.example.interviewchallenge.ui.ui_utils.ChangeStatusBarAndNavigationBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChangeStatusBarAndNavigationBarColor(
                context = this,
                isDarkMode = isSystemInDarkTheme()
            )
            CompositionLocalProvider(LocalLayoutDirection.provides(LayoutDirection.Rtl)) {
                InterviewChallengeTheme {
                    val navController = rememberNavController()
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NavGraph(
                            navController = navController,
                            scannerViewModel = hiltViewModel(),
                            appDatabaseViewModel = hiltViewModel(),
                            paddingValues = innerPadding
                        )
                    }
                }
            }

        }
    }
}
