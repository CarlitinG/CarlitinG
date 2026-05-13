# Weather App - Aplicación de Notificaciones del Clima

Una aplicación Flutter para Android e iOS que te notifica sobre las condiciones climáticas.

## Características

- 🌤️ Muestra el clima actual basado en tu ubicación o una ciudad específica
- 🔔 Notificaciones locales para condiciones climáticas severas
- 🌍 Búsqueda de cualquier ciudad en el mundo
- 📱 Interfaz moderna y responsiva
- 🌙 Soporte para modo oscuro/claro

## Requisitos Previos

1. **Flutter SDK** (versión 3.0.0 o superior)
   - Instala Flutter desde: https://docs.flutter.dev/get-started/install

2. **API Key de OpenWeatherMap** (gratuita)
   - Regístrate en: https://openweathermap.org/api
   - Obtén tu API key gratuita

## Configuración

### 1. Configurar la API Key

Abre el archivo `lib/services/weather_service.dart` y reemplaza:

```dart
static const String _apiKey = 'TU_API_KEY_AQUI';
```

con tu API key real de OpenWeatherMap.

### 2. Instalar dependencias

```bash
cd weather_app
flutter pub get
```

### 3. Configurar permisos

#### Android
En `android/app/src/main/AndroidManifest.xml`, agrega:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

#### iOS
En `ios/Runner/Info.plist`, agrega:

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>Necesitamos tu ubicación para mostrar el clima local</string>
<key>NSLocationAlwaysUsageDescription</key>
<string>Necesitamos tu ubicación para mostrar el clima local</string>
```

## Ejecutar la App

### En un emulador/dispositivo Android:

```bash
flutter run
```

### En un simulador/dispositivo iOS:

```bash
flutter run
```

## Estructura del Proyecto

```
weather_app/
├── lib/
│   ├── main.dart                 # Punto de entrada
│   ├── models/
│   │   └── weather_model.dart    # Modelo de datos del clima
│   ├── services/
│   │   ├── weather_service.dart  # Servicio de API del clima
│   │   └── notification_service.dart  # Servicio de notificaciones
│   ├── providers/
│   │   └── weather_provider.dart # Provider para gestión de estado
│   └── screens/
│       └── home_screen.dart      # Pantalla principal
├── test/
│   └── widget_test.dart
├── pubspec.yaml                  # Dependencias
└── README.md
```

## Funcionalidades

### 1. Detección Automática de Ubicación
La app solicita permiso para acceder a tu ubicación y muestra el clima local automáticamente.

### 2. Búsqueda Manual
Puedes buscar el clima de cualquier ciudad escribiendo su nombre.

### 3. Notificaciones de Alerta
Cuando se detectan condiciones climáticas severas (temperaturas extremas, tormentas, etc.), la app envía una notificación automática si tienes las notificaciones activadas.

### 4. Actualización Manual
Toca el botón de refrescar en la barra de aplicaciones o desliza hacia abajo para actualizar los datos.

## Dependencias Principales

- `http`: Para hacer solicitudes a la API de OpenWeatherMap
- `flutter_local_notifications`: Para notificaciones locales
- `geolocator`: Para obtener la ubicación del dispositivo
- `provider`: Para gestión de estado
- `shared_preferences`: Para almacenamiento local
- `intl`: Para formateo de fechas

## Notas Importantes

⚠️ **Sin API Key**: La app funcionará con datos simulados si no configuras una API key válida.

🔒 **Permisos**: Asegúrate de otorgar los permisos de ubicación y notificaciones cuando la app los solicite.

## Solución de Problemas

### La app no muestra el clima real
- Verifica que hayas configurado correctamente tu API key en `weather_service.dart`
- Asegúrate de tener conexión a internet

### Las notificaciones no funcionan
- Verifica que los permisos de notificación estén activados en la configuración del dispositivo
- En Android 13+, debes aceptar explícitamente el permiso de notificaciones

### Error de ubicación
- Asegúrate de que los servicios de ubicación estén activados en tu dispositivo
- Verifica que hayas otorgado permisos de ubicación a la app

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## Contribuciones

¡Las contribuciones son bienvenidas! Siéntete libre de enviar issues y pull requests.
