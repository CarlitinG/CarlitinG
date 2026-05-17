# 🌤️ WeatherKotlin - App del Clima en Kotlin

Aplicación nativa de Android desarrollada en **Kotlin** que muestra el clima actual y envía notificaciones de alertas climáticas.

## Características

- ✅ Clima en tiempo real usando OpenWeatherMap API
- ✅ Detección automática de ubicación GPS
- ✅ Búsqueda de ciudades por nombre
- ✅ Notificaciones de alertas climáticas (temperaturas extremas, tormentas)
- ✅ Interfaz moderna con Material Design
- ✅ Soporte para modo oscuro/claro

## Configuración

La aplicación ya incluye tu API Key de OpenWeatherMap: `e1d364f4513badbfae5bea7e72021142`

## Requisitos

- Android Studio Arctic Fox o superior
- Android SDK 24 o superior
- Dispositivo físico o emulador con servicios de Google Play

## Cómo ejecutar

1. Abre el proyecto en Android Studio
2. Espera a que Gradle sincronice las dependencias
3. Conecta tu dispositivo Android o inicia un emulador
4. Presiona el botón "Run" (▶️) o ejecuta `./gradlew installDebug`

## Estructura del Proyecto

```
app/src/main/java/com/example/weatherkotlin/
├── MainActivity.kt              # Actividad principal
├── model/
│   └── WeatherResponse.kt       # Modelos de datos
├── network/
│   ├── WeatherApiService.kt     # Interfaz de la API
│   └── RetrofitClient.kt        # Cliente Retrofit
├── repository/
│   └── WeatherRepository.kt     # Repositorio de datos
└── notification/
    ├── NotificationHelper.kt    # Helper de notificaciones
    └── BootReceiver.kt          # Receiver para inicio del sistema
```

## Permisos Requeridos

- `INTERNET` - Para llamar a la API
- `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` - Para obtener ubicación
- `POST_NOTIFICATIONS` - Para enviar notificaciones (Android 13+)

## API Key

Este proyecto usa la API Key proporcionada: `e1d364f4513badbfae5bea7e72021142`

Si necesitas cambiarla, edita `MainActivity.kt` y `WeatherRepository.kt`.

## Licencia

MIT License
