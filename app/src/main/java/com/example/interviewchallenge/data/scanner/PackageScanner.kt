package com.example.interviewchallenge.data.scanner

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
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
import androidx.core.graphics.createBitmap

class PackageScanner @Inject constructor(
    private val appDao: AppDao,
    private val permissionDao: PermissionDao,
    private val pm: PackageManager,
    private val riskAnalyzer: RiskAnalyzer,
) {

    suspend fun startScan(
        onProgress: (scanned: Int, total: Int, suspicious: Int, remaining: Int, appName: String, icon: Bitmap) -> Unit
    ) {
        val flagsToQuery = (PackageManager.GET_PERMISSIONS or PackageManager.GET_SERVICES).toLong()
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(flagsToQuery)
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getInstalledPackages(PackageManager.GET_PERMISSIONS or PackageManager.GET_SERVICES)
        }

        val total = packages.size
        var scanned = 0
        var suspiciousCount = 0

        for (pkg in packages) {
            yield()
            var currentAppName = ""
            var currentIcon: Bitmap? = null

            try {
                val app = pkg.applicationInfo
                if (app == null) {
                    scanned++
                    val remaining = total - scanned
                    onProgress(scanned, total, suspiciousCount, remaining, "", Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
                    continue
                }

                val packageName = pkg.packageName
                val appName = app.loadLabel(pm).toString()
                currentAppName = appName
                val drawable = app.loadIcon(pm)
                val icon = drawable.toBitmap()
                currentIcon = icon

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

                val installer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    try {
                        pm.getInstallSourceInfo(packageName).installingPackageName
                    } catch (_: Exception) {
                        null
                    }
                } else {
                    @Suppress("DEPRECATION")
                    pm.getInstallerPackageName(packageName)
                }

                val issideloaded = installer == null || installer == "com.android.vending"

                appDao.insert(appInfo)

                val permissionList = mutableListOf<PermissionInfo>()

                pkg.requestedPermissions?.let { perms ->
                    val flags = pkg.requestedPermissionsFlags
                    val mapped = perms.mapIndexed { i, p ->
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
                    }
                    permissionList.addAll(mapped)
                }

                // Inspect <service> tags for BIND_ACCESSIBILITY_SERVICE
                val hasAccessibilityService = pkg.services?.any { service ->
                    service.permission == Manifest.permission.BIND_ACCESSIBILITY_SERVICE
                } ?: false

                if (hasAccessibilityService && permissionList.none { it.permissionName == Manifest.permission.BIND_ACCESSIBILITY_SERVICE }) {
                    permissionList.add(
                        PermissionInfo(
                            packageName = packageName,
                            permissionName = Manifest.permission.BIND_ACCESSIBILITY_SERVICE,
                            dangerous = true,
                            granted = true
                        )
                    )
                }

                val distinctList = permissionList.distinctBy { it.permissionName }
                permissionDao.insertAll(distinctList)
            } catch (e: Exception) {
                Log.w("PackageScanner", "Skipping ${pkg.packageName}", e)
            }

            scanned++
            val remaining = total - scanned
            onProgress(
                scanned,
                total,
                suspiciousCount,
                remaining,
                currentAppName,
                currentIcon ?: createBitmap(1, 1)
            )
        }
    }
}
