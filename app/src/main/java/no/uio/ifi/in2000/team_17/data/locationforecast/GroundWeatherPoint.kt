package no.uio.ifi.in2000.team_17.data.locationforecast

data class GroundWeatherPoint (
    val windSpeed: Double?,
    val windFromDirection: Double?,
    val airTemperature: Double?,
    val pressureSeaLevel: Double?,
    val cloudFraction: Double?,
    val rain: Double?,
    val humidity: Double?,
    val height: Double?,
)