package edu.ucne.taskmaster.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.taskmaster.data.local.database.TaskMask
import edu.ucne.taskmaster.remote.api.LabelApi
import edu.ucne.taskmaster.remote.api.TasksApi
import edu.ucne.taskmaster.remote.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module

object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): TaskMask {
        return Room.databaseBuilder(
            appContext,
            TaskMask::class.java,
            "TaskMask.db"
        ).fallbackToDestructiveMigration().build()
    }

    private const val BASE_URL = "https://taskmasterapi.azurewebsites.net/"

    @Singleton
    @Provides
    fun TasksApi(): TasksApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TasksApi::class.java)
    }

    @Singleton
    @Provides
    fun LabelApi(): LabelApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(LabelApi::class.java)
    }

    @Singleton
    @Provides
    fun UserApi(): UserApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }
}