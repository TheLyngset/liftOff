package no.uio.ifi.in2000.team_17.data.isobaricgrib.model


// May be used instead of keeping up with a multitude of lists and their indexes
data class WindPoint(
    val windSpeed: Double = 0.0,
    val windFromDirection: Double = 0.0,
    val temperature: Double = 0.0,
    val pressure: Int = 1000,
    val height: Double = 0.0
)

