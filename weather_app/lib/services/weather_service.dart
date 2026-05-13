import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/weather_model.dart';

class WeatherService {
  // NOTA: Debes reemplazar esto con tu propia API key de OpenWeatherMap
  // Regístrate en https://openweathermap.org/api para obtener una gratis
  static const String _apiKey = 'e1d364f4513badbfae5bea7e72021142';
  static const String _baseUrl = 'https://api.openweathermap.org/data/2.5';

  // Para pruebas, usaremos datos simulados si no hay API key
  bool get hasApiKey => _apiKey != 'TU_API_KEY_AQUI' && _apiKey.isNotEmpty;

  Future<WeatherModel> getCurrentWeather({
    double? latitude,
    double? longitude,
    String city = 'London',
  }) async {
    if (!hasApiKey) {
      // Datos simulados para pruebas
      return _getMockWeatherData(city);
    }

    try {
      String url;
      if (latitude != null && longitude != null) {
        url = '$_baseUrl/weather?lat=$latitude&lon=$longitude&appid=$_apiKey&units=metric';
      } else {
        url = '$_baseUrl/weather?q=$city&appid=$_apiKey&units=metric';
      }

      final response = await http.get(Uri.parse(url));

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        return WeatherModel.fromJson(data);
      } else {
        throw Exception('Failed to load weather data: ${response.statusCode}');
      }
    } catch (e) {
      print('Error fetching weather: $e');
      // Retornar datos simulados en caso de error
      return _getMockWeatherData(city);
    }
  }

  Future<WeatherModel> _getMockWeatherData(String city) {
    final now = DateTime.now();
    return WeatherModel(
      city: city,
      country: 'UK',
      temperature: 18.5 + (now.hour % 5),
      description: 'Partly cloudy',
      icon: '02d',
      humidity: 65,
      windSpeed: 3.2,
      timestamp: now,
    );
  }

  Future<bool> checkSevereWeather(WeatherModel weather) async {
    // Verificar condiciones climáticas severas
    if (weather.temperature > 35 || weather.temperature < -5) {
      return true;
    }
    if (weather.description.toLowerCase().contains('storm') ||
        weather.description.toLowerCase().contains('thunder') ||
        weather.description.toLowerCase().contains('rain')) {
      return true;
    }
    return false;
  }
}
