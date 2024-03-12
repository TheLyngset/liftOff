package no.uio.ifi.in2000.team_17.data.isobaricgrib.model


// May be used instead of keeping up with a multitude of lists and their indexes
data class WeatherPoint(
    val windSpeed: Double = 0.0,
    val windDirection: Double = 0.0,
    val windShear:Double = 0.0,
    val temperature: Double = 0.0,
    val pressure: Double = 1000.00,
    val height: Double = 0.0
)

