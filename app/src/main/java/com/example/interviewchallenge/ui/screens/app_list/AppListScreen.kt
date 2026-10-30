package com.example.interviewchallenge.ui.screens.app_list

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.interviewchallenge.R
import com.example.interviewchallenge.ui.navigation.Screens
import com.example.interviewchallenge.ui.ui_utils.AppTopBar
import com.example.interviewchallenge.viewmodel.AppDatabaseViewModel

@Composable
fun AppListScreen(
    navController: NavController,
    databaseViewModel: AppDatabaseViewModel
) {
    Scaffold(
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_app_list),
                onClick = { navController.popBackStack() }
            )
        },
        content = { paddingValues ->
            AppListSection(
                paddingValues = paddingValues,
                databaseViewModel = databaseViewModel,
                onAppClick = { packageName ->
                    navController.navigate(Screens.AppDetails(packageName = packageName.trim()))
                }
            )
        }
    )
}
