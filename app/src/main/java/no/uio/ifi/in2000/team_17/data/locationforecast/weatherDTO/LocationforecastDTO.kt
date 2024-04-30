package no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO

data class LocationforecastDTO(
    val geometry: Geometry = Geometry(),
    val properties: Properties = Properties(),
    val type: String = ""
)