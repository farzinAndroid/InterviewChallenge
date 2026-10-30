package com.example.interviewchallenge.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

object PackageUtils {

    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun createUninstallIntent(packageName: String): Intent {
        val packageUri = Uri.fromParts("package", packageName, null)
        return Intent(Intent.ACTION_DELETE, packageUri).apply {
            putExtra(Intent.EXTRA_RETURN_RESULT, true)
        }
    }
}
