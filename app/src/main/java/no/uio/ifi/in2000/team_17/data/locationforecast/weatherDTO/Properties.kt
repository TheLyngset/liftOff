package no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO

data class Properties(
    val meta: Meta = Meta(),
    val timeseries: List<Timesery> = listOf(Timesery())
)