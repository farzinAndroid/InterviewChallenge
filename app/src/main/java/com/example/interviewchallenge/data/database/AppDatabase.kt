package com.example.interviewchallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.interviewchallenge.data.database.dao.AppDao
import com.example.interviewchallenge.data.database.dao.PermissionDao
import com.example.interviewchallenge.data.model.AppInfo
import com.example.interviewchallenge.data.model.PermissionInfo

@Database(
    entities = [
        AppInfo::class,
        PermissionInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    abstract fun permissionDao(): PermissionDao
}
