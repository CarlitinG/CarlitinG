import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:provider/provider.dart';
import 'package:weather_app/main.dart';
import 'package:weather_app/providers/weather_provider.dart';
import 'package:weather_app/services/weather_service.dart';
import 'package:weather_app/services/notification_service.dart';
import 'package:weather_app/models/weather_model.dart';

void main() {
  testWidgets('La app muestra la pantalla principal', (WidgetTester tester) async {
    // Crear servicios mock
    final weatherService = WeatherService();
    final notificationService = NotificationService();
    
    await tester.pumpWidget(
      MultiProvider(
        providers: [
          ChangeNotifierProvider(
            create: (_) => WeatherProvider(weatherService, notificationService),
          ),
        ],
        child: const WeatherApp(),
      ),
    );

    // Verificar que la app carga
    expect(find.byType(MaterialApp), findsOneWidget);
    expect(find.text('Weather Notifier'), findsOneWidget);
  });

  test('WeatherModel se crea correctamente', () {
    final weather = WeatherModel(
      city: 'Madrid',
      country: 'ES',
      temperature: 25.5,
      description: 'Soleado',
      icon: '01d',
      humidity: 45,
      windSpeed: 3.2,
      timestamp: DateTime.now(),
    );

    expect(weather.city, 'Madrid');
    expect(weather.country, 'ES');
    expect(weather.temperature, 25.5);
    expect(weather.description, 'Soleado');
    expect(weather.humidity, 45);
    expect(weather.windSpeed, 3.2);
    expect(weather.temperatureCelsius, '25.5°C');
    expect(weather.fullLocation, 'Madrid, ES');
  });

  test('WeatherService detecta clima severo correctamente', () async {
    final service = WeatherService();
    
    // Temperatura extrema (> 35°C)
    final hotWeather = WeatherModel(
      city: 'Sevilla',
      country: 'ES',
      temperature: 40.0,
      description: 'Soleado',
      icon: '01d',
      humidity: 20,
      windSpeed: 2.0,
      timestamp: DateTime.now(),
    );
    
    expect(await service.checkSevereWeather(hotWeather), true);

    // Tormenta
    final stormWeather = WeatherModel(
      city: 'London',
      country: 'UK',
      temperature: 15.0,
      description: 'Thunderstorm',
      icon: '11d',
      humidity: 80,
      windSpeed: 10.0,
      timestamp: DateTime.now(),
    );
    
    expect(await service.checkSevereWeather(stormWeather), true);

    // Clima normal
    final normalWeather = WeatherModel(
      city: 'Paris',
      country: 'FR',
      temperature: 22.0,
      description: 'Partly cloudy',
      icon: '02d',
      humidity: 55,
      windSpeed: 3.5,
      timestamp: DateTime.now(),
    );
    
    expect(await service.checkSevereWeather(normalWeather), false);
  });
}
