package com.example.interviewchallenge.ui.screens.app_details

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.interviewchallenge.R
import com.example.interviewchallenge.data.model.AppInfo
import com.example.interviewchallenge.data.model.PermissionInfo
import com.example.interviewchallenge.ui.theme.AccentPurpleColor
import com.example.interviewchallenge.ui.theme.BackgroundColor
import com.example.interviewchallenge.ui.theme.spacing
import com.example.interviewchallenge.ui.ui_utils.AppTopBar
import com.example.interviewchallenge.utils.DigitHelper
import com.example.interviewchallenge.utils.PackageUtils
import com.example.interviewchallenge.viewmodel.AppDatabaseViewModel

@Composable
fun AppDetailsScreen(
    databaseViewModel: AppDatabaseViewModel,
    packageName: String,
    navController: NavController
) {
    val context = LocalContext.current

    val appDetails by databaseViewModel.appByPackage.collectAsStateWithLifecycle()
    val permissions by databaseViewModel.allPermissions.collectAsStateWithLifecycle()

    LaunchedEffect(packageName) {
        databaseViewModel.getAppByPackage(packageName)
        databaseViewModel.getPermissions(packageName)
    }

    val uninstallLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (!PackageUtils.isPackageInstalled(context, packageName)) {
            databaseViewModel.deleteAppByPackage(packageName)
            Toast.makeText(context, R.string.app_uninstalled_successfully, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    val isSystem = appDetails?.isSystem ?: false

    Scaffold(
        containerColor = MaterialTheme.colorScheme.BackgroundColor,
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            AppTopBar(
                topBarText = stringResource(R.string.topbar_title_app_details),
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            AppDetailsBottomBar(
                isSystemApp = isSystem,
                onInfoClicked = {
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = "package:$packageName".toUri()
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        Toast.makeText(
                            context,
                            R.string.error_action_not_supported,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onUninstallClicked = {
                    try {
                        val intent = PackageUtils.createUninstallIntent(packageName)
                        uninstallLauncher.launch(intent)
                    } catch (_: Exception) {
                        Toast.makeText(
                            context,
                            R.string.error_action_not_supported,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    ) { innerPadding ->
        AppDetailsContent(
            paddingValues = innerPadding,
            appInfo = appDetails,
            permissions = permissions
        )
    }
}

@Composable
private fun AppDetailsContent(
    paddingValues: PaddingValues,
    appInfo: AppInfo?,
    permissions: List<PermissionInfo>
) {
    val distinctPermissions = remember(permissions) {
        permissions.distinctBy { it.permissionName }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp16),
        contentPadding = PaddingValues(vertical = MaterialTheme.spacing.dp16)
    ) {
        appInfo?.let { app ->

            item { AppHeaderSection(appInfo = app) }
            item { TechnicalDetailsCard(appInfo = app) }
            item {
                val countFa = DigitHelper.digitByLang(distinctPermissions.size.toString())
                Text(
                    text = stringResource(R.string.requested_permissions_count, countFa),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.AccentPurpleColor,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.dp8)
                )
            }
            if (distinctPermissions.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_permissions_requested),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                items(
                    items = distinctPermissions,
                    key = { perm -> if (perm.id != 0L) perm.id else perm.permissionName }
                ) { perm ->
                    PermissionItemCard(permission = perm)
                }
            }
        }
    }
}


