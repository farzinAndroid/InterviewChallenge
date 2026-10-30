package com.example.interviewchallenge.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewchallenge.repository.ScannerRepository
import com.example.interviewchallenge.ui.ui_utils.ScanPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository
) : ViewModel() {


    val totalCount = MutableStateFlow(0)
    val scannedCount = MutableStateFlow(0)
    val suspiciousCount = MutableStateFlow(0)
    val remainingCount = MutableStateFlow(0)
    val appName = MutableStateFlow("")
    val iconBitmap = MutableStateFlow<Bitmap?>(null)
    val scanState = MutableStateFlow(ScanPageState.IDLE)
    private var scanJob: Job? = null

    fun resetScanState() {
        cancelScanJob()
        scanState.value = ScanPageState.IDLE
        totalCount.value = 0
        scannedCount.value = 0
        suspiciousCount.value = 0
        remainingCount.value = 0
        appName.value = ""
        iconBitmap.value = null
    }

    fun cancelScanJob() {
        scanJob?.cancel()
        scanJob = null
        scanState.value = ScanPageState.IDLE
    }

    fun startScan() {
        if (scanJob?.isActive == true) return
        scanJob = viewModelScope.launch(Dispatchers.IO) {
            scanState.emit(ScanPageState.SCANNING)


            scannerRepository.startScan(
                onProgress = { scanned, total, suspicious, remaining, app, icon ->
                    totalCount.value = total
                    scannedCount.value = scanned
                    suspiciousCount.value = suspicious
                    remainingCount.value = remaining
                    appName.value = app
                    iconBitmap.value = icon
                }
            )

            if (!isActive) return@launch

            scanState.emit(ScanPageState.SCAN_COMPLETE)
        }
    }
}
