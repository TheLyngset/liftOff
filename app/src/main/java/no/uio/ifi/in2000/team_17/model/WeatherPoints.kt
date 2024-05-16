package no.uio.ifi.in2000.team_17.model

import no.uio.ifi.in2000.team_17.R


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
    var maxWindShear: WindShear = WindShear(),
    val maxWind: WindLayer = WindLayer(),
    val temperature: Double = -1.0,
    val pressure: Double = -1.0,
    val height: Double = -1.0,
    var cloudFraction: Double = -1.0,
    val rain: Rain = Rain(),
    var humidity: Double = -1.0,
    var dewPoint: Double = -1.0,
    var fog: Double = -1.0,
    val available: Available = Available()
)
{
    operator fun iterator(): List<Pair<WeatherParameter, Any>> {
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
 * A data class containing lists for each weather parameter where indexes correspond to hours in the future.
 * The lists may have different lengths depending on what data is available.
 *
 * @property dateTime List of date and time strings.
 * @property date List of date strings.
 * @property time List of time strings.
 * @property groundWind List of ground wind data.
 * @property maxWindShear List of maximum wind shear data.
 * @property maxWind List of maximum wind data.
 * @property cloudFraction List of cloud fraction data.
 * @property rain List of rain data.
 * @property humidity List of humidity data.
 * @property dewPoint List of dew point data.
 * @property fog List of fog data.
 * @property temperature List of temperature data.
 * @property updated String representing the last update time.
 * @property availableIndexes Object of type [AvailableIndexes] containing information on how many data-points are available for each parameter.
 *
 * @constructor Initializes the availableIndexes property based on the size of each list.
 */
data class WeatherDataLists(
    val dateTime: List<String> = listOf("0000-01-01T00:00:00"),
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

    /**
     * Returns a [WeatherPointInTime] at the specified index.
     *
     * @param index The index of the weather data point.
     * @return A [WeatherPointInTime] containing the weather data at the specified index.
     * If the index is out of bounds for a certain parameter, the default value for that parameter is used.
     */
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
            temperature = temperature.getOrElse(index) { 0.0 },
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

    /**
     * Returns a list of pairs where each pair consists of a [WeatherParameter] and a list of corresponding weather data.
     *
     * @return A list of pairs. Each pair consists of a [WeatherParameter] and a list of corresponding weather data.
     */
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
    var speed: Double = 0.0,
    val height: Double = 0.0,
    val direction: Double = 0.0
) {
    override fun toString(): String {
        return speed.toString()
    }
}

/**
 * A data class holding relevant values for WindShear
 */
data class WindShear(
    var speed: Double = 0.0,
    val height: Double = 0.0
) {
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
    var probability: Double = 0.0
) {
    override fun toString(): String {
        return probability.toString()
    }
}

/**
 * An enum class representing different types of weather parameters. Each parameter has a title, a unit, and an image associated with it.
 *
 * @property titleId The resource ID for the title of the weather parameter.
 * @property unitId The resource ID for the unit of the weather parameter. Can be null if the parameter does not have a unit.
 * @property imageID The resource ID for the image representing the weather parameter. Can be null if the parameter does not have an associated image.
 */
enum class WeatherParameter(
    val titleId: Int,
    val unitId: Int?,
    val imageID: Int?
) {
    DATE(R.string.date, null, null),
    TIME(R.string.time, null, null),
    GROUNDWIND(R.string.groundWind_title, R.string.m_s, R.drawable.groundwind2),
    MAXWINDSHEAR(R.string.maxShear_title, R.string.m_s, R.drawable.shearwind),
    MAXWIND(R.string.maxAirWind_title, R.string.m_s, R.drawable.wind),
    CLOUDFRACTION(R.string.cloudiness_title, R.string.percentage, R.drawable.cloud),
    RAIN(R.string.rain_title, R.string.percentage, R.drawable.rain),
    HUMIDITY(R.string.humidity_title, R.string.percentage, R.drawable.humidity),
    DEWPOINT(R.string.dewPoint_title, R.string.celsiusDegree, R.drawable.dewpoint),
    FOG(R.string.fog_title, R.string.percentage, R.drawable.fog),
    MARGIN(R.string.safety_margin_title, null, null)
}
