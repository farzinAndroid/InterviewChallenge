package com.example.interviewchallenge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppInfo(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val versionName: String,
    val apkSize: Long,
    val installTime: String,
    val isSystem: Boolean,
    val riskScore: Int,
    val riskLevel: String,
    val suspicious: Boolean
)
