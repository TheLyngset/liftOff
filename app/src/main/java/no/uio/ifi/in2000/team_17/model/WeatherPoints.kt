package no.uio.ifi.in2000.team_17.model

// data class with relevant date for a weather point
data class WeatherPointLayer(
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

data class WeatherPointInTime(
    val date: String = "",
    val time: String = "",
    val groundWind: WindLayer = WindLayer(),
    val maxWindShear: WindShear = WindShear(),
    val maxWind: WindLayer =  WindLayer(),
    val temperature: Double = -1.0,
    val pressure: Double = -1.0,
    val height: Double = -1.0,
    val cloudFraction: Double = -1.0,
    val rain: Rain = Rain(),
    val humidity: Double = -1.0,
    val dewPoint: Double = -1.0,
    val fog: Double = -1.0,
    val available: Available = Available()
)

/*//object holding lists of values for each variable that needs to be displyed and used in the resultsUI
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
)*/

/*
A data class containing lists for each parameter where indexes correspond to hours in the future
*/
data class WeatherDataLists(
    val date: List<String> = listOf(),
    val time: List<String> = listOf(),
    val groundWind: List<WindLayer> = listOf(),
    val maxWindShear: List<WindShear> = listOf(),
    val maxWind: List<WindLayer> = listOf(),
    val cloudFraction: List<Double> = listOf(),
    val rain: List<Rain> = listOf(),
    val humidity: List<Double> = listOf(),
    val dewPoint: List<Double> = listOf(),
    val fog: List<Double> = listOf(),
    val temperature : List<Double> = listOf(),
    val updated: String = "00",
    var availableIndexes: AvailableIndexes = AvailableIndexes()
){
    fun get(index: Int): WeatherPointInTime {
        return WeatherPointInTime(
            date = date.getOrElse(index){""},
            time = time.getOrElse(index){"00"},
            groundWind = groundWind.getOrElse(index){ WindLayer() },
            maxWindShear = maxWindShear.getOrElse(index){ WindShear() },
            maxWind = maxWind.getOrElse(index){WindLayer()},
            cloudFraction = cloudFraction.getOrElse(index){0.0},
            rain = rain.getOrElse(index){ Rain() },
            humidity = humidity.getOrElse(index){0.0},
            dewPoint = dewPoint.getOrElse(index){0.0},
            fog = fog.getOrElse(index){0.0},
            available = Available(
                date = availableIndexes.date > index,
                time = availableIndexes.time > index,
                groundWind = availableIndexes.groundWind > index,
                maxWindShear = availableIndexes.maxWindShear > index,
                maxWind = availableIndexes.maxWind > index,
                cloudFraction = availableIndexes.cloudFraction > index,
                rain = availableIndexes.rain > index,
                humidity = availableIndexes.humidity > index,
                dewPoint = availableIndexes.dewPoint > index,
                fog = availableIndexes.fog > index,
                temperature = availableIndexes.temperature > index
            )
        )
    }
    init {
        availableIndexes = AvailableIndexes(
            date = date.size,
            time = time.size,
            groundWind = groundWind.size,
            maxWindShear = maxWindShear.size,
            maxWind = maxWind.size,
            cloudFraction = cloudFraction.size,
            rain = rain.size,
            humidity = humidity.size,
            dewPoint = dewPoint.size,
            fog = fog.size,
            temperature = temperature.size
        )
    }
}

data class AvailableIndexes(
    val date: Int = 0,
    val time: Int = 0,
    val groundWind: Int = 0,
    val maxWindShear: Int = 0,
    val maxWind: Int = 0,
    val cloudFraction: Int = 0,
    val rain: Int = 0,
    val humidity: Int = 0,
    val dewPoint: Int = 0,
    val fog: Int = 0,
    val temperature: Int = 0
)
data class Available(
    val date:Boolean = false,
    val time:Boolean = false,
    val groundWind:Boolean = false,
    val maxWindShear:Boolean = false,
    val maxWind:Boolean = false,
    val cloudFraction:Boolean = false,
    val rain:Boolean = false,
    val humidity:Boolean = false,
    val dewPoint:Boolean = false,
    val fog: Boolean = false,
    val temperature : Boolean = false
){
    fun get(title : String): Boolean {
        return when(title){
            "Ground Wind" -> groundWind
            "Max Wind" -> maxWind
            "Max Shear" -> maxWindShear
            "Temperature" -> temperature
            "Cloudiness" -> cloudFraction
            "Rain" -> rain
            "Humidity" -> humidity
            "Fog" -> fog
            "Dew Point" -> dewPoint
            else -> false
        }
    }
}

data class WindLayer(
    val speed: Double = 0.0,
    val height: Double = 0.0,
    val direction: Double = 0.0
)
data class WindShear(
    val speed: Double = 0.0,
    val height: Double = 0.0
)
data class Rain(
    val min: Double = 0.0,
    val median: Double = 0.0,
    val max: Double = 0.0
)

enum class WeatherParameter{
    TIME,
    GROUNDWIND,
    MAXWINDSHEAR,
    MAXWIND,
    CLOUDFRACTION,
    RAIN,
    HUMIDITY,
    DEWPOINT,
    FOG
}
