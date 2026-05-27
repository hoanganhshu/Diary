package com.diary.app.demo.di

import android.content.Context
import androidx.room.Room
import com.diary.app.demo.data.local.SaveDiary
import com.diary.app.demo.data.local.WeatherApi
import com.diary.app.demo.data.local.WeatherService
import com.diary.app.demo.data.local.dao.TodoDaoDiary
import com.diary.app.demo.data.repository.FileRepository
import com.diary.app.demo.data.repository.SecurityRepository
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
     fun provideWeather( ): WeatherApi {
        return WeatherService.weatherApi
    }
    @Provides
    fun provideTodoDaoDiary(db: SaveDiary): TodoDaoDiary {
        return db.todoDao()
    }


    @Provides
    @Singleton
    fun providerDiaryDatabase(@ApplicationContext context: Context) : SaveDiary {
        return  Room.databaseBuilder(context.applicationContext, SaveDiary::class.java, name = "Diary").fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFileRepository(
        @ApplicationContext context: Context
    ): FileRepository {
        return FileRepository(context)
    }

    @Singleton
    @Provides
    fun provideSecurityPrefs(
        @ApplicationContext context: Context
    ): SecurityRepository = SecurityRepository(context)





}