package com.diary.app.demo.data.local

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {
        private const val Base_url = "https://api.openweathermap.org/"

        val api: Retrofit by lazy {
            Retrofit.Builder().baseUrl(Base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        val weatherApi: WeatherApi by lazy {
            api.create(WeatherApi::class.java)

        }

    }