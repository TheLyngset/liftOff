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


//object holding lists of values for each variable that needs to be displyed and used in the resultsUI
data class WeatherPointsResults(
    var date: MutableList<String> = mutableListOf(),
    var time: MutableList<String> = mutableListOf(),
    var groundWindSpeed: MutableList<Double> = mutableListOf(),
    var windDirection: MutableList<Double> = mutableListOf(),
    var maxWindShear: MutableList<Double> = mutableListOf(),
    var maxWindSpeed: MutableList<Double> = mutableListOf(),
    var cloudFraction: MutableList<Double> = mutableListOf(),
    var rain: MutableList<Double> = mutableListOf(),
    var humidity: MutableList<Double> = mutableListOf(),
    var dewPoint: MutableList<Double> = mutableListOf(),
    var fog: MutableList<Double> = mutableListOf()
)

