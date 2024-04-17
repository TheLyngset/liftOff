package no.uio.ifi.in2000.team_17.model;

data class Thresholds(
    val groundWindSpeed: Double = 8.6,
    val maxWindSpeed: Double = 17.2,
    val maxWindShear: Double = 24.5,
    val cloudFraction: Double = 15.0,
    val fog: Double = 0.001,
    val rain: Double = 0.001,
    val humidity: Double = 75.0,
    val dewPoint: Double = 15.0,
    val margin: Double = 0.6
)
