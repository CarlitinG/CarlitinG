package com.example.weatherkotlin.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val name: String,
    val main: MainWeather,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys,
    val dt: Long
)

data class MainWeather(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,
    val deg: Int?
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
