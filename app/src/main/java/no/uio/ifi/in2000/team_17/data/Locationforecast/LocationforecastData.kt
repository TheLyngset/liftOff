package no.uio.ifi.in2000.team_17.data.Locationforecast

data class LocationforecastAllData(
    val type: String?,
    val geometry: Geometry?,
    val properties: Properties?
)
 data class Geometry(
     val type: String,
     val coordinates: List<Double>
 )

data class Properties(
    val properties: Meta,
    val timeseries: LocationforcastTimeSeries
)
data class Meta(
    val updated_at: String,
    val units: LocationforecastUnits
)
data class LocationforcastTimeSeries (
    val data: List<LocationforcastTimeSerie>
)
data class LocationforcastTimeSerie (
    val time: String,
    val data: LocationforecastData
)
data class LocationforecastData (
    val instant: LocationforecastInstant,
    val next_12_hours: LocationforecastNextHours,
    val next_1_hours: LocationforecastNextHours,
    val next_6_hours: LocationforecastNextHours,
)
data class LocationforecastDetails (
    val   air_pressure_at_sea_level: Double,
    val air_temperature: Double,
    val cloud_area_fraction: Double,
    val precipitation_amount: Double,
    val relative_humidity: Double,
    val wind_from_direction: Double,
    val wind_speed: Double
)
data class LocationforecastInstant (
    val instant: LocationforecastDetails
)
data class LocationforecastNextHours(
    val summary: String,
    val details: LocationforecastDetails
)