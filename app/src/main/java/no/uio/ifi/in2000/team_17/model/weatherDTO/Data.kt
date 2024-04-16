package no.uio.ifi.in2000.team_17.model.weatherDTO

data class Data(
    val instant: Instant = Instant(),
    val next_12_hours: Next12Hours = Next12Hours(),
    val next_1_hours: Next1Hours = Next1Hours(),
    val next_6_hours: Next6Hours = Next6Hours()
)