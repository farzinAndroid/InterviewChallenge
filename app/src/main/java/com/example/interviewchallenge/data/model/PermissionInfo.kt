package com.example.interviewchallenge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "permissions",
    primaryKeys = ["packageName", "permissionName"]
)
data class PermissionInfo(
    val packageName: String,
    val permissionName: String,
    val dangerous: Boolean,
    val granted: Boolean
)
