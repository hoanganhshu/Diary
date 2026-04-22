package viewmodel

import androidx.lifecycle.ViewModel



import com.diary.app.demo.database.WeatherApi
import com.diary.app.demo.database.WeatherDatabase

import com.diary.app.demo.database.WeatherService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetWeatherApi @Inject constructor( private val api: WeatherApi) : ViewModel(){


   suspend fun getWeather(lat: String, lon: String, apiKey : String ) : WeatherDatabase {
        return WeatherService.weatherApi.getCurrentWeather(lat,lon,apiKey)
    }
}