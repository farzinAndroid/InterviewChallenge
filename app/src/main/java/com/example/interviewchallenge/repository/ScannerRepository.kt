package com.example.interviewchallenge.repository

import android.graphics.Bitmap
import com.example.interviewchallenge.data.scanner.PackageScanner
import javax.inject.Inject

class ScannerRepository @Inject constructor(
    private val packageScanner: PackageScanner
) {

    suspend fun startScan(
        onProgress: (scanned: Int, total: Int, suspicious: Int, remaining: Int, appName: String, iconBitmap: Bitmap) -> Unit
    ) = packageScanner.startScan(onProgress = onProgress)

}
