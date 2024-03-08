package no.uio.ifi.in2000.team_17.data.locationforecast.jsondata.dto.weather

data class Properties(
    val meta: Meta,
    val timeseries: List<Timesery>
)