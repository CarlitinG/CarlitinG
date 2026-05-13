import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';
import '../models/weather_model.dart';
import '../services/weather_service.dart';
import '../services/notification_service.dart';

class WeatherProvider extends ChangeNotifier {
  final WeatherService _weatherService;
  final NotificationService _notificationService;

  WeatherModel? _currentWeather;
  bool _isLoading = false;
  String? _error;
  bool _notificationsEnabled = false;
  Position? _currentPosition;

  WeatherProvider(this._weatherService, this._notificationService);

  WeatherModel? get currentWeather => _currentWeather;
  bool get isLoading => _isLoading;
  String? get error => _error;
  bool get notificationsEnabled => _notificationsEnabled;
  Position? get currentPosition => _currentPosition;

  Future<void> requestLocationPermission() async {
    try {
      LocationPermission permission = await Geolocator.checkPermission();
      
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          _error = 'Permiso de ubicación denegado';
          notifyListeners();
          return;
        }
      }

      if (permission == LocationPermission.deniedForever) {
        _error = 'Permiso de ubicación denegado permanentemente';
        notifyListeners();
        return;
      }

      // Obtener posición actual
      _currentPosition = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );
      
      notifyListeners();
    } catch (e) {
      _error = 'Error al obtener ubicación: $e';
      notifyListeners();
    }
  }

  Future<void> requestNotificationPermission() async {
    await _notificationService.requestPermissions();
    _notificationsEnabled = true;
    notifyListeners();
  }

  Future<void> fetchWeather({String? city}) async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      if (_currentPosition != null && city == null) {
        _currentWeather = await _weatherService.getCurrentWeather(
          latitude: _currentPosition!.latitude,
          longitude: _currentPosition!.longitude,
        );
      } else {
        _currentWeather = await _weatherService.getCurrentWeather(
          city: city ?? 'London',
        );
      }

      // Verificar condiciones severas y enviar notificación si es necesario
      if (_notificationsEnabled && _currentWeather != null) {
        final isSevere = await _weatherService.checkSevereWeather(_currentWeather!);
        if (isSevere) {
          await _notificationService.showSevereWeatherAlert(
            city: _currentWeather!.city,
            description: _currentWeather!.description,
            temperature: _currentWeather!.temperature,
          );
        }
      }

      _isLoading = false;
      notifyListeners();
    } catch (e) {
      _error = 'Error al obtener el clima: $e';
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> refreshWeather() async {
    await fetchWeather();
  }

  void toggleNotifications(bool value) {
    _notificationsEnabled = value;
    notifyListeners();
  }
}
