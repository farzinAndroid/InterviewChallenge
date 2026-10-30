package com.example.interviewchallenge.data.scanner

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import com.example.interviewchallenge.data.analyzer.RiskAnalyzer
import com.example.interviewchallenge.data.database.dao.AppDao
import com.example.interviewchallenge.data.database.dao.PermissionDao
import com.example.interviewchallenge.data.model.AppInfo
import com.example.interviewchallenge.data.model.PermissionInfo
import com.example.interviewchallenge.utils.DateTimeUtils
import kotlinx.coroutines.yield
import java.io.File
import javax.inject.Inject

class PackageScanner @Inject constructor(
    private val appDao: AppDao,
    private val permissionDao: PermissionDao,
    private val pm: PackageManager,
    private val riskAnalyzer: RiskAnalyzer,
) {

    suspend fun startScan(
        onProgress: (scanned: Int, total: Int, suspicious: Int, remaining: Int, appName: String, icon: Bitmap) -> Unit
    ) {
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(
                    PackageManager.GET_PERMISSIONS.toLong()
                )
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        }

        val total = packages.size
        var scanned = 0
        var suspiciousCount = 0

        for (pkg in packages) {
            yield()
            val app = pkg.applicationInfo ?: continue
            val packageName = pkg.packageName
            val appName = app.loadLabel(pm).toString()
            val drawable = app.loadIcon(pm)
            val icon = drawable.toBitmap()

            val apk = File(app.sourceDir)
            val apkSize = if (apk.exists()) apk.length() else 0L

            val versionName = pkg.versionName ?: ""
            val installTime = DateTimeUtils.formatDateTime(pkg.firstInstallTime)

            val result = riskAnalyzer.analyze(pkg, app)
            val risk = result.score
            val level = result.level
            val isSuspicious = risk >= 40

            if (isSuspicious) {
                suspiciousCount++
            }

            val appInfo = AppInfo(
                packageName = packageName,
                appName = appName,
                versionName = versionName,
                apkSize = apkSize,
                installTime = installTime,
                isSystem = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                riskScore = risk,
                riskLevel = level,
                suspicious = isSuspicious
            )

            appDao.insert(appInfo)

            pkg.requestedPermissions?.let { perms ->
                val flags = pkg.requestedPermissionsFlags
                val list = perms.mapIndexed { i, p ->
                    val granted = flags != null && i < flags.size && (flags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
                    val isDangerous = try {
                        val pInfo = pm.getPermissionInfo(p, 0)
                        (pInfo.protectionLevel and android.content.pm.PermissionInfo.PROTECTION_DANGEROUS) != 0 ||
                        p == Manifest.permission.SYSTEM_ALERT_WINDOW ||
                        p == Manifest.permission.REQUEST_INSTALL_PACKAGES ||
                        p == Manifest.permission.BIND_ACCESSIBILITY_SERVICE
                    } catch (_: Exception) {
                        false
                    }
                    PermissionInfo(
                        packageName = packageName,
                        permissionName = p,
                        dangerous = isDangerous,
                        granted = granted
                    )
                }.distinctBy { it.permissionName }
                permissionDao.insertAll(list)
            }

            scanned++
            val remaining = total - scanned
            onProgress(scanned, total, suspiciousCount, remaining, appName, icon)
        }
    }
}
