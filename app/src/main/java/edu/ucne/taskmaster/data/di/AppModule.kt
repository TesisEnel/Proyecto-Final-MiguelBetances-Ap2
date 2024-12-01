package edu.ucne.taskmaster.data.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.taskmaster.data.local.database.TaskMask
import edu.ucne.taskmaster.remote.api.LabelApi
import edu.ucne.taskmaster.remote.api.TaskLabelApi
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

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateAdater())
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder().build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun TasksApi(moshi: Moshi): TasksApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TasksApi::class.java)
    }

    @Singleton
    @Provides
    fun LabelApi(moshi: Moshi): LabelApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LabelApi::class.java)
    }

    @Singleton
    @Provides
    fun UserApi(moshi: Moshi): UserApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun TaskLabelApi(moshi: Moshi): TaskLabelApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TaskLabelApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTaskDao(database: TaskMask) = database.taskDao()

    @Singleton
    @Provides
    fun provideLabelDao(database: TaskMask) = database.labelDao()

    @Singleton
    @Provides
    fun provideUserDao(database: TaskMask) = database.userDao()

    @Singleton
    @Provides
    fun provideTaskLabelDao(database: TaskMask) = database.taskLabelDao()


}