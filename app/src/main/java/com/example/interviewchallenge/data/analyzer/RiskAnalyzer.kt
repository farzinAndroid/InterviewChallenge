package com.example.interviewchallenge.data.analyzer

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import com.example.interviewchallenge.data.model.RiskLevels
import com.example.interviewchallenge.data.model.RiskResult
import javax.inject.Inject

class RiskAnalyzer @Inject constructor() {

    fun analyze(
        pkg: PackageInfo,
        app: ApplicationInfo
    ): RiskResult {
        var score = 0

        if ((app.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            score += 15
        }

        pkg.requestedPermissions?.forEach { permission ->
            when (permission) {
                Manifest.permission.SYSTEM_ALERT_WINDOW -> score += 25
                Manifest.permission.REQUEST_INSTALL_PACKAGES -> score += 25

                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS -> score += 20

                Manifest.permission.MANAGE_EXTERNAL_STORAGE -> score += 25
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> score += 15
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO -> score += 15

                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION -> score += 15
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG -> score += 15

                Manifest.permission.CAMERA -> score += 10
                Manifest.permission.RECORD_AUDIO -> score += 10
                Manifest.permission.RECEIVE_BOOT_COMPLETED -> score += 10
            }
        }

        // Check <service> tags for BIND_ACCESSIBILITY_SERVICE
        val hasAccessibilityService = pkg.services?.any { service ->
            service.permission == Manifest.permission.BIND_ACCESSIBILITY_SERVICE
        } ?: false

        if (hasAccessibilityService) {
            score += 30
        }

        if (score > 100) {
            score = 100
        }

        val level = when {
            score >= 70 -> RiskLevels.HIGH.name
            score >= 40 -> RiskLevels.MEDIUM.name
            else -> RiskLevels.SAFE.name
        }

        return RiskResult(
            score = score,
            level = level
        )
    }
}
