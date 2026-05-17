package com.example.weatherkotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherkotlin.databinding.ActivityMainBinding
import com.example.weatherkotlin.model.WeatherResponse
import com.example.weatherkotlin.repository.WeatherRepository
import com.example.weatherkotlin.notification.NotificationHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var notificationHelper: NotificationHelper

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineLocationGranted || coarseLocationGranted) {
            loadWeather()
        } else {
            Toast.makeText(this, "Permiso de ubicación requerido", Toast.LENGTH_LONG).show()
            // Cargar clima por defecto (London) si no hay permiso
            loadWeatherByCity("London")
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar repositorio con tu API Key
        weatherRepository = WeatherRepository("e1d364f4513badbfae5bea7e72021142")
        notificationHelper = NotificationHelper(this)

        setupUI()
        checkPermissions()
    }

    private fun setupUI() {
        binding.refreshButton.setOnClickListener {
            checkPermissions()
        }

        binding.searchButton.setOnClickListener {
            val city = binding.cityInput.text.toString().trim()
            if (city.isNotEmpty()) {
                loadWeatherByCity(city)
            } else {
                Toast.makeText(this, "Ingresa una ciudad", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() {
        // Verificar permisos de ubicación
        val fineLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocation != PackageManager.PERMISSION_GRANTED || 
            coarseLocation != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            loadWeather()
        }

        // Verificar permisos de notificación (Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            )
            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun loadWeather() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val weather = weatherRepository.getWeatherByLocation(this@MainActivity)
                displayWeather(weather)
                
                // Verificar alertas de clima severo
                checkWeatherAlerts(weather)
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error al obtener clima: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                showLoading(false)
            }
        }
    }

    private fun loadWeatherByCity(city: String) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val weather = weatherRepository.getWeatherByCity(city)
                displayWeather(weather)
                checkWeatherAlerts(weather)
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Ciudad no encontrada o error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                showLoading(false)
            }
        }
    }

    private fun displayWeather(weather: WeatherResponse) {
        showLoading(false)
        
        binding.cityName.text = weather.name
        binding.temperature.text = "${weather.main.temp.toInt()}°C"
        binding.description.text = weather.weather.first().description.capitalize()
        binding.humidity.text = "Humedad: ${weather.main.humidity}%"
        binding.windSpeed.text = "Viento: ${weather.wind.speed} m/s"
        binding.feelsLike.text = "Sensación: ${weather.main.feelsLike.toInt()}°C"

        // Icono basado en el clima
        val iconResId = getWeatherIcon(weather.weather.first().icon)
        binding.weatherIcon.setImageResource(iconResId)
    }

    private fun checkWeatherAlerts(weather: WeatherResponse) {
        val temp = weather.main.temp
        val description = weather.weather.first().description.lowercase()
        
        var alertMessage: String? = null
        
        // Alertas por temperatura extrema
        if (temp > 35) {
            alertMessage = "⚠️ ALERTA: Temperatura muy alta (${temp.toInt()}°C). ¡Mantente hidratado!"
        } else if (temp < 0) {
            alertMessage = "⚠️ ALERTA: Temperatura bajo cero (${temp.toInt()}°C). ¡Abrígate bien!"
        }
        
        // Alertas por condiciones severas
        if (description.contains("storm") || description.contains("thunder")) {
            alertMessage = "⛈️ ALERTA: Tormenta detectada. ¡Busca refugio!"
        } else if (description.contains("rain") && temp < 5) {
            alertMessage = "🌧️ ALERTA: Lluvia fría. ¡Ten cuidado!"
        }

        if (alertMessage != null) {
            notificationHelper.sendNotification("Alerta Climática", alertMessage)
            Toast.makeText(this, alertMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.refreshButton.isEnabled = !isLoading
        binding.searchButton.isEnabled = !isLoading
    }

    private fun getWeatherIcon(iconCode: String): Int {
        return when {
            iconCode.contains("01d") -> android.R.drawable.sun
            iconCode.contains("01n") -> android.R.drawable.ic_dialog_info
            iconCode.contains("02") -> android.R.drawable.ic_dialog_info
            iconCode.contains("03") || iconCode.contains("04") -> android.R.drawable.ic_dialog_info
            iconCode.contains("09") || iconCode.contains("10") -> android.R.drawable.ic_dialog_info
            iconCode.contains("11") -> android.R.drawable.ic_dialog_info
            iconCode.contains("13") -> android.R.drawable.ic_dialog_info
            iconCode.contains("50") -> android.R.drawable.ic_dialog_info
            else -> android.R.drawable.ic_dialog_info
        }
    }
}
