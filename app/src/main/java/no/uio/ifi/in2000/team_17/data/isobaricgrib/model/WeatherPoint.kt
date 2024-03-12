package no.uio.ifi.in2000.team_17.data.isobaricgrib.model


// May be used instead of keeping up with a multitude of lists and their indexes
data class WeatherPoint(
    val windSpeed: Double?,
    val windFromDirection: Double?,
    val temperature: Double?,
    val pressure: Double?,
    val height: Double?
)

