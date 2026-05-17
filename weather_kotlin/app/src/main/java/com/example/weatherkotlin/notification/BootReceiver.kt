package com.example.weatherkotlin.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.weatherkotlin.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-programar notificaciones o verificar clima al iniciar
            checkWeatherOnBoot(context)
        }
    }
    
    private fun checkWeatherOnBoot(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = WeatherRepository("e1d364f4513badbfae5bea7e72021142")
                // Podrías implementar lógica para verificar clima automáticamente
                // y enviar notificación si hay alertas
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
