package com.example.interviewchallenge.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.interviewchallenge.ui.screens.app_details.AppDetailsScreen
import com.example.interviewchallenge.ui.screens.app_list.AppListScreen
import com.example.interviewchallenge.ui.screens.home.HomeScreen
import com.example.interviewchallenge.viewmodel.AppDatabaseViewModel
import com.example.interviewchallenge.viewmodel.ScannerViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screens = Screens.Home,
    scannerViewModel: ScannerViewModel,
    appDatabaseViewModel: AppDatabaseViewModel,
    paddingValues: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable<Screens.Home> {
                HomeScreen(
                    scannerViewModel = scannerViewModel,
                    databaseViewModel = appDatabaseViewModel,
                    navController = navController,
                    paddingValues = paddingValues
                )
            }

            composable<Screens.AppList> {
                AppListScreen(
                    navController = navController,
                    databaseViewModel = appDatabaseViewModel
                )
            }

            composable<Screens.AppDetails> { backStackEntry ->
                val appDetails = backStackEntry.toRoute<Screens.AppDetails>()
                AppDetailsScreen(
                    navController = navController,
                    packageName = appDetails.packageName,
                    databaseViewModel = appDatabaseViewModel
                )
            }
        }
    }
}
