package com.example.interviewchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.interviewchallenge.data.database.AppDatabase
import com.example.interviewchallenge.data.database.dao.AppDao
import com.example.interviewchallenge.data.database.dao.PermissionDao
import com.example.interviewchallenge.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            name = Constants.APP_DB_NAME,
            klass = AppDatabase::class.java
        )
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideAppDao(database: AppDatabase): AppDao {
        return database.appDao()
    }

    @Provides
    fun providePermissionDao(database: AppDatabase): PermissionDao {
        return database.permissionDao()
    }
}
