package no.uio.ifi.in2000.team_17.model.weatherDTO

data class Properties(
    val meta: Meta = Meta(),
    val timeseries: List<Timesery> = listOf(Timesery())
)