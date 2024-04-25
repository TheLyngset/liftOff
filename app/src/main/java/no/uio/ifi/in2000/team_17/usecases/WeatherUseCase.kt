package no.uio.ifi.in2000.team_17.usecases

import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WeatherPointLayer
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor

//har tilgang til repository og returnerer use case data. F. eks temperatur
//henter data fra alle repo som trengs.
class WeatherUseCase {
    //Thresholds:
    //Vind på bakken: 8.6 m/s
    //Vind i lufta: 17.2 m/s
    //Shear vind: 24.5 m/s
    //
    //Sikt: 15% cloud coverage
    //Tåke: 0
    //
    //Nedbør: 0
    //Luftfuktighet: 75%
    //Dew point: max 15 grader

    companion object {
        @JvmStatic
        fun canLaunch(
            weatherPointInTime: WeatherPointInTime,
            threshholds: Thresholds
        ): TrafficLightColor {
            //tåke --- (connected to clouds, dew point and precipitation)
            if(
                    weatherPointInTime.groundWind.speed.toDouble() < threshholds.maxWindSpeed &&
                            weatherPointInTime.humidity.toDouble() < threshholds.humidity &&
                            weatherPointInTime.dewPoint.toDouble() < threshholds.dewPoint &&
                            weatherPointInTime.cloudFraction.toDouble() < threshholds.cloudFraction &&
                            weatherPointInTime.rain.probability.toDouble() < threshholds.rain &&
                            weatherPointInTime.fog.toDouble() < threshholds.fog &&
                            weatherPointInTime.maxWind.speed.toDouble() < threshholds.maxWindSpeed &&
                            weatherPointInTime.maxWindShear.speed.toDouble() < threshholds.maxWindShear
                    ){
                return if(
                    weatherPointInTime.groundWind.speed.toDouble() < threshholds.maxWindSpeed * threshholds.margin &&
                    weatherPointInTime.humidity.toDouble() < threshholds.humidity * threshholds.margin &&
                    weatherPointInTime.dewPoint.toDouble() < threshholds.dewPoint * threshholds.margin &&
                    weatherPointInTime.cloudFraction.toDouble() < threshholds.cloudFraction * threshholds.margin &&
                    weatherPointInTime.rain.probability.toDouble() < threshholds.rain * threshholds.margin &&
                    weatherPointInTime.fog.toDouble() < threshholds.fog * threshholds.margin &&
                    weatherPointInTime.maxWind.speed.toDouble() < threshholds.maxWindSpeed * threshholds.margin &&
                    weatherPointInTime.maxWindShear.speed.toDouble() < threshholds.maxWindShear * threshholds.margin
                ){
                    TrafficLightColor.GREEN
                } else{
                    TrafficLightColor.YELLOW
                }
            }
            return TrafficLightColor.RED
        }
    }
}


internal fun launchClearance(
    weatherPoint: WeatherPointLayer,
    maxWindSpeed: Double,
    maxShearWind: Double
): Boolean {
    return (
            weatherPoint.windSpeed < 8.6 &&
                    weatherPoint.humidity < 75.0 &&
                    weatherPoint.dewPoint < 15.0 &&
                    weatherPoint.cloudFraction < 15.0 &&
                    weatherPoint.rain < 0.1 &&
                    weatherPoint.windShear < 24.5 &&
                    weatherPoint.fog < 0.1 &&
                    maxWindSpeed < 17.2 &&
                    maxShearWind < 24.5)
}