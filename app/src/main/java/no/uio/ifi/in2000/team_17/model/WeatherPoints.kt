package no.uio.ifi.in2000.team_17.model

/**
 * A data class used when parsing isobaric data and finding maxWind and maxShear
 */
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

/**
 * a data class containing all the relevant data for a given point in time.
 * variables are non nullable, but the datastructure contains an available object
 * used to only show data when it is actually available
 */
data class WeatherPointInTime(
    val date: String = "00",
    val time: String = "00",
    val groundWind: WindLayer = WindLayer(),
    val maxWindShear: WindShear = WindShear(),
    val maxWind: WindLayer = WindLayer(),
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
/**
 * A data class containing lists for each parameter where indexes correspond to hours in the future
 * The lists may have different lengths depending on what data is available
 * availableIndexes contains information on how many data-points is available for each parameter
 * [WeatherDataLists.get] returns a [WeatherPointInTime] at the corresponding index
 * [WeatherDataLists.iterator] iterates over the parameters and returns a pair of
 * [WeatherParameter] and the corresponding parameter
 */
data class WeatherDataLists(
    val date: List<String> = listOf("0000-00-00"),
    val time: List<String> = listOf(""),
    val groundWind: List<WindLayer> = listOf(WindLayer()),
    val maxWindShear: List<WindShear> = listOf(WindShear()),
    val maxWind: List<WindLayer> = listOf(WindLayer()),
    val cloudFraction: List<Double> = listOf(-1.0),
    val rain: List<Rain> = listOf(Rain()),
    val humidity: List<Double> = listOf(-1.0),
    val dewPoint: List<Double> = listOf(-1.0),
    val fog: List<Double> = listOf(-1.0),
    val temperature: List<Double> = listOf(-1.0),
    val updated: String = "00",
    var availableIndexes: AvailableIndexes = AvailableIndexes()
) {
    fun get(index: Int): WeatherPointInTime {
        return WeatherPointInTime(
            date = date.getOrElse(index) { "" },
            time = time.getOrElse(index) { "00" },
            groundWind = groundWind.getOrElse(index) { WindLayer() },
            maxWindShear = maxWindShear.getOrElse(index) { WindShear() },
            maxWind = maxWind.getOrElse(index) { WindLayer() },
            cloudFraction = cloudFraction.getOrElse(index) { 0.0 },
            rain = rain.getOrElse(index) { Rain() },
            humidity = humidity.getOrElse(index) { 0.0 },
            dewPoint = dewPoint.getOrElse(index) { 0.0 },
            fog = fog.getOrElse(index) { 0.0 },
            temperature = temperature.getOrElse(index){0.0},
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

    operator fun iterator(): List<Pair<WeatherParameter, List<Any>>> {
        return listOf(
            WeatherParameter.DATE to date,
            WeatherParameter.TIME to time,
            WeatherParameter.MAXWIND to maxWind,
            WeatherParameter.MAXWINDSHEAR to maxWindShear,
            WeatherParameter.GROUNDWIND to groundWind,
            WeatherParameter.RAIN to rain,
            WeatherParameter.HUMIDITY to humidity,
            WeatherParameter.CLOUDFRACTION to cloudFraction,
            WeatherParameter.DEWPOINT to dewPoint,
            WeatherParameter.FOG to fog
        )
    }
}

/**
 * a data class used to easily know how many indexes each parameter in WeatherDataLists has
 */
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


/**
 * a data class used to easily know if a parameter in [WeatherPointInTime] is available
 */
data class Available(
    val date: Boolean = false,
    val time: Boolean = false,
    val groundWind: Boolean = false,
    val maxWindShear: Boolean = false,
    val maxWind: Boolean = false,
    val cloudFraction: Boolean = false,
    val rain: Boolean = false,
    val humidity: Boolean = false,
    val dewPoint: Boolean = false,
    val fog: Boolean = false,
    val temperature: Boolean = false
) {
    fun get(type: WeatherParameter): Boolean {
        return when (type) {
            WeatherParameter.GROUNDWIND -> groundWind
            WeatherParameter.MAXWINDSHEAR -> maxWindShear
            WeatherParameter.MAXWIND -> maxWind
            WeatherParameter.CLOUDFRACTION -> cloudFraction
            WeatherParameter.RAIN -> rain
            WeatherParameter.HUMIDITY -> humidity
            WeatherParameter.DEWPOINT -> dewPoint
            WeatherParameter.FOG -> fog
            else -> false
        }
    }
}

/**
 * A data class holding relevant values for wind
 */
data class WindLayer(
    val speed: Double = 0.0,
    val height: Double = 0.0,
    val direction: Double = 0.0
)
{
    override fun toString(): String {
        return speed.toString()
    }
}
/**
 * A data class holding relevant values for WindShear
 */
data class WindShear(
    val speed: Double = 0.0,
    val height: Double = 0.0
)
{
    override fun toString(): String {
        return speed.toString()
    }
}
/**
 * a data class holding relevant values for rain
 */
data class Rain(
    val min: Double = 0.0,
    val median: Double = 0.0, // is this precipitation amount? There is no median in the API
    val max: Double = 0.0,
    val probability: Double = 0.0
){
    override fun toString(): String {
        return probability.toString()
    }
}

enum class WeatherParameter(val title: String) {
    DATE("Date"),
    TIME("Time"),
    GROUNDWIND("Ground Wind"),
    MAXWINDSHEAR("Wind Shear"),
    MAXWIND("Max Wind"),
    CLOUDFRACTION("Cloud Fraction"),
    RAIN("Rain"),
    HUMIDITY("Humidity"),
    DEWPOINT("Dew point"),
    FOG("Fog"),
    MARGIN("margin")
}
