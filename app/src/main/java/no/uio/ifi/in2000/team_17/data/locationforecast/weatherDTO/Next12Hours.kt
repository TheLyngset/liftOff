package no.uio.ifi.in2000.team_17.data.locationforecast.weatherDTO

data class Next12Hours(
    val details: DetailsX = DetailsX(),
    val summary: Summary = Summary()
)