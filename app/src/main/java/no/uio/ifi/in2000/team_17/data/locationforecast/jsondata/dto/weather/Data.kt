package no.uio.ifi.in2000.team_17.data.locationforecast.jsondata.dto.weather

data class Data(
    val instant: Instant,
    val next_12_hours: Next12Hours,
    val next_1_hours: Next1Hours,
    val next_6_hours: Next6Hours
)