package no.uio.ifi.in2000.team_17.model.weatherDTO

data class LocationforecastDTO(
    val geometry: Geometry = Geometry(),
    val properties: Properties = Properties(),
    val type: String = ""
)