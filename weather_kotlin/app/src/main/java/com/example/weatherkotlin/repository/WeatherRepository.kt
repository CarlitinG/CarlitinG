package com.example.weatherkotlin.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherkotlin.model.WeatherResponse
import com.example.weatherkotlin.network.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class WeatherRepository(private val apiKey: String) {
    
    private val weatherApi = RetrofitClient.weatherApiService
    
    suspend fun getWeatherByCity(city: String): WeatherResponse {
        return weatherApi.getWeatherByCity(city, apiKey)
    }
    
    suspend fun getWeatherByLocation(context: Context): WeatherResponse {
        val location = getCurrentLocation(context)
        return weatherApi.getWeatherByLocation(
            lat = location.latitude,
            lon = location.longitude,
            apiKey = apiKey
        )
    }
    
    private suspend fun getCurrentLocation(context: Context): Location {
        val fusedLocationClient: FusedLocationProviderClient = 
            LocationServices.getFusedLocationProviderClient(context)
        
        // Verificar permisos
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException("Permiso de ubicación no concedido")
        }
        
        // Obtener última ubicación conocida
        var location: Location? = fusedLocationClient.lastLocation.await()
        
        // Si no hay ubicación, solicitar actualización
        if (location == null) {
            throw Exception("No se pudo obtener la ubicación. Asegúrate de tener GPS activado.")
        }
        
        return location
    }
}
