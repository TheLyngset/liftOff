package no.uio.ifi.in2000.team_17.model.weatherDTO

data class Details(
    val air_pressure_at_sea_level: Double = 0.0,
    val air_temperature: Double= 0.0,
    val air_temperature_percentile_10: Double= 0.0,
    val air_temperature_percentile_90: Double= 0.0,
    val cloud_area_fraction: Double= 0.0,
    val cloud_area_fraction_high: Double= 0.0,
    val cloud_area_fraction_low: Double= 0.0,
    val cloud_area_fraction_medium: Double= 0.0,
    val dew_point_temperature: Double= 0.0,
    val fog_area_fraction: Double= 0.0,
    val relative_humidity: Double= 0.0,
    val ultraviolet_index_clear_sky: Double= 0.0,
    val wind_from_direction: Double= 0.0,
    val wind_speed: Double= 0.0,
    val wind_speed_of_gust: Double= 0.0,
    val wind_speed_percentile_10: Double= 0.0,
    val wind_speed_percentile_90: Double= 0.0
)