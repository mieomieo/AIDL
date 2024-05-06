package com.example.server.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.server.dao.StudentDao
import com.example.server.database.StudentDatabase
import com.example.server.repository.StudentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): StudentDatabase {
        Log.e("Runnn","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        return Room.databaseBuilder(
            application.applicationContext, StudentDatabase::class.java, "student_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStudentDao(appDatabase: StudentDatabase): StudentDao {
        return appDatabase.studentDao()
    }

    @Provides
    @Singleton
    fun provideStudentRepository(studentDao: StudentDao): StudentRepository {
        return StudentRepository(studentDao)
    }
}