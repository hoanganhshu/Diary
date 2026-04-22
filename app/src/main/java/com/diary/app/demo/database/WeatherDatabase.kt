package com.diary.app.demo.database



data class MainInfo(
    val temp: Double?
     )


data class WeatherCondition(
    val main: String?,
    val description: String?
)


data class WeatherDatabase(
    val name: String? = null,
    val main: MainInfo?,
    val weather: List<WeatherCondition>?
)


