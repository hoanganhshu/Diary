package com.diary.app.demo

import android.content.Context
import androidx.room.Room
import com.diary.app.demo.database.SaveDiary
import com.diary.app.demo.database.WeatherApi
import com.diary.app.demo.database.WeatherService
import com.diary.app.demo.database.dao.TodoDaoDiary
import com.diary.app.demo.model.AddDiaryModel
import com.diary.app.demo.model.SecurityModel
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
    fun provideAddDiaryModel(
        @ApplicationContext context: Context
    ): AddDiaryModel {
        return AddDiaryModel(context)
    }

    @Singleton
    @Provides
    fun provideSecurityPrefs(
        @ApplicationContext context: Context
    ): SecurityModel = SecurityModel(context)





}