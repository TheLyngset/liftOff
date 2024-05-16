package no.uio.ifi.in2000.team_17.usecases

import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor

class WeatherUseCase {
    //Thresholds:
    //Vind på bakken: 8.6 m/s
    //Vind i lufta: 17.2 m/s
    //Shear vind: 24.5 m/s
    //Sikt: 15% cloud coverage
    //Tåke: 0
    //Nedbør: 0 //probability = 10 %
    //Luftfuktighet: 75%
    //Dew point: max 15 grader

    companion object {
        /*
        * prupose: inform user whether the rocket can be launched based on whether on the date/time in the location chosen
        * @param weatherPointInTime contains all the relevant weather data
        * @param thresholds contains all the relevant thresholds set by the user or default
        * calculates min and max values for each parameter based on the margin set by default or by the user
        * returns green if each value is below the minimum margin
        * returns yellow  if each value is in range of the min and max margin or above the max margin
        * returns red if one of the values is above the max margin
         */
        @JvmStatic
        fun canLaunch(
            weatherPointInTime: WeatherPointInTime,
            thresholds: Thresholds
        ): TrafficLightColor {
            val minMarginGroundWind = thresholds.groundWindSpeed * thresholds.margin
            val maxMarginGroundWind = 2 * thresholds.groundWindSpeed - minMarginGroundWind
            val minMarginHumidity = thresholds.humidity * thresholds.margin
            val maxMarginHumidity = 2 * thresholds.humidity - minMarginHumidity
            val minMarginDewPoint = thresholds.dewPoint * thresholds.margin
            val maxMarginDewPoint = 2 * thresholds.dewPoint - minMarginDewPoint
            val minMarginCloudFraction = thresholds.cloudFraction * thresholds.margin
            val maxMarginCloudFraction = 2 * thresholds.cloudFraction - minMarginDewPoint
            val minMarginRainProbability = thresholds.rain * thresholds.margin
            val maxMarginRainProbability = 2 * thresholds.rain - minMarginRainProbability
            val minMarginFog = thresholds.fog * thresholds.margin
            val maxMarginFog = 2 * thresholds.fog - minMarginFog
            val minMarginAirWind = thresholds.maxWindSpeed * thresholds.margin
            val maxMarginAirWind = 2 * thresholds.maxWindSpeed - minMarginAirWind
            val minMarginWindShear = thresholds.maxWindShear * thresholds.margin
            val maxMarginWindShear = 2 * thresholds.maxWindShear - minMarginWindShear

            if (
                weatherPointInTime.groundWind.speed <= minMarginGroundWind &&
                weatherPointInTime.humidity <= minMarginHumidity &&
                weatherPointInTime.dewPoint <= minMarginDewPoint &&
                weatherPointInTime.cloudFraction <= minMarginCloudFraction &&
                weatherPointInTime.rain.probability <= minMarginRainProbability &&
                weatherPointInTime.fog <= minMarginFog &&
                weatherPointInTime.maxWind.speed <= minMarginAirWind &&
                weatherPointInTime.maxWindShear.speed <= minMarginWindShear
            ) return TrafficLightColor.GREEN

            if (
                weatherPointInTime.groundWind.speed <= maxMarginGroundWind &&
                weatherPointInTime.humidity <= maxMarginHumidity &&
                weatherPointInTime.dewPoint <= maxMarginDewPoint &&
                weatherPointInTime.cloudFraction <= maxMarginCloudFraction &&
                weatherPointInTime.rain.probability <= maxMarginRainProbability &&
                weatherPointInTime.fog <= maxMarginFog &&
                weatherPointInTime.maxWind.speed <= maxMarginAirWind &&
                weatherPointInTime.maxWindShear.speed <= maxMarginWindShear
            ) return TrafficLightColor.YELLOW

            return TrafficLightColor.RED
        }
        @JvmStatic
        fun calculateColor(type: WeatherParameter, value: String, thresholds: Thresholds): TrafficLightColor {
            return when(type){
                WeatherParameter.CLOUDFRACTION-> canLaunch(WeatherPointInTime(cloudFraction = value.toDouble()), thresholds)
                WeatherParameter.GROUNDWIND -> canLaunch(WeatherPointInTime(groundWind = WindLayer(value.toDouble())), thresholds)
                WeatherParameter.MAXWINDSHEAR -> canLaunch(WeatherPointInTime(maxWindShear = WindShear(value.toDouble())), thresholds)
                WeatherParameter.MAXWIND -> canLaunch(WeatherPointInTime(maxWind = WindLayer(value.toDouble())), thresholds)
                WeatherParameter.RAIN -> canLaunch(WeatherPointInTime(rain = Rain(median = value.toDouble())), thresholds)
                WeatherParameter.HUMIDITY -> canLaunch(WeatherPointInTime(humidity = value.toDouble()), thresholds)
                WeatherParameter.DEWPOINT -> canLaunch(WeatherPointInTime(dewPoint = value.toDouble()), thresholds)
                WeatherParameter.FOG -> canLaunch(WeatherPointInTime(fog = value.toDouble()), thresholds)
                else -> TrafficLightColor.WHITE
            }
        }
    }
}
