package com.example.client.di

import android.content.Context
import com.example.client.repository.StudentRepository
import com.example.client.service.ServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStudentServiceConnection(@ApplicationContext context: Context): ServiceManager {
        return ServiceManager(context)
    }

    @Provides
    @Singleton
    fun provideStudentRepository(studentServiceConnection: ServiceManager): StudentRepository {
        return StudentRepository(studentServiceConnection)
    }
}