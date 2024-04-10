package no.uio.ifi.in2000.team_17.model

// data class with relevant date for a weather point
data class WeatherPointOld(
    val date: String? = "",
    val time: String? = "",
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

data class WeatherPointNew(
    val date: String? = "",
    val time: String? = "",
    val groundWindSpeed: Double = -1.0,
    val windDirection: Double = -1.0,
    val maxWindShear: Double = -1.0,
    val maxWindSpeed: Double = -1.0,
    val temperature: Double = -1.0,
    val pressure: Double = -1.0,
    val height: Double = -1.0,
    val cloudFraction: Double = -1.0,
    val rain: Double = -1.0,
    val humidity: Double = -1.0,
    val dewPoint: Double = -1.0,
    val fog: Double = -1.0
)

//list with weather data over time and when the list was last updated
data class WeatherPointList(
    val updated: String = "",
    val weatherPoints: List<WeatherPointOld> = listOf(WeatherPointOld())
)

data class WeatherPointsResults(
    val date: List<String> = emptyList(),
    val time: List<String> = emptyList(),
    val groundWindSpeed: List<Double> = emptyList(),
    val windDirection: List<Double> = emptyList(),
    val maxWindShear: List<Double> = emptyList(),
    val maxWindSpeed: List<Double> = emptyList(),
    val cloudFraction: List<Double> = emptyList(),
    val rain: List<Double> = emptyList(),
    val humidity: List<Double> = emptyList(),
    val dewPoint: List<Double> = emptyList(),
    val fog: List<Double> = emptyList()
)


