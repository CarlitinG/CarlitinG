class WeatherModel {
  final String city;
  final String country;
  final double temperature;
  final String description;
  final String icon;
  final int humidity;
  final double windSpeed;
  final DateTime timestamp;

  WeatherModel({
    required this.city,
    required this.country,
    required this.temperature,
    required this.description,
    required this.icon,
    required this.humidity,
    required this.windSpeed,
    required this.timestamp,
  });

  factory WeatherModel.fromJson(Map<String, dynamic> json) {
    return WeatherModel(
      city: json['name'] ?? 'Unknown',
      country: json['sys']['country'] ?? '',
      temperature: (json['main']['temp'] as num).toDouble(),
      description: json['weather'][0]['description'] ?? '',
      icon: json['weather'][0]['icon'] ?? '',
      humidity: json['main']['humidity'] ?? 0,
      windSpeed: (json['wind']['speed'] as num).toDouble(),
      timestamp: DateTime.now(),
    );
  }

  String get temperatureCelsius => '${temperature.toStringAsFixed(1)}°C';
  
  String get fullLocation => '$city, $country';
}
