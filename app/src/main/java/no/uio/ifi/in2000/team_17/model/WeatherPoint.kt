package no.uio.ifi.in2000.team_17.model


// May be used instead of keeping up with a multitude of lists and their indexes
data class WeatherPoint(
    val windSpeed: Double = -1.0,
    val windDirection: Double = -1.0,
    val windShear: Double = -1.0,
    val temperature: Double = -1.0,
    val pressure: Double = -1.0,
    val height: Double = -1.0,
    val cloudFraction: Double = -1.0,
    val rain: Double = -1.0,
    val humidity: Double = -1.0,
    val dewPoint: Double = -1.0,
    val fog: Double = -1.0
)

