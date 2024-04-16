package no.uio.ifi.in2000.team_17.data

import no.uio.ifi.in2000.team17.AdvancedSettings
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WeatherPointLayer

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
            threshholds: AdvancedSettings
        ): Boolean {
            //tåke --- (connected to clouds, dew point and precipitation)
            return (
                    weatherPointInTime.groundWind.speed < threshholds.maxWindSpeed &&
                            weatherPointInTime.humidity < threshholds.humidity &&
                            weatherPointInTime.dewPoint < threshholds.dewPoint &&
                            weatherPointInTime.cloudFraction < threshholds.cloudFraction &&
                            weatherPointInTime.rain.median < threshholds.rain &&
                            weatherPointInTime.fog < threshholds.fog &&
                            weatherPointInTime.maxWind.speed < threshholds.maxWindSpeed &&
                            weatherPointInTime.maxWindShear.speed < threshholds.maxWindShear
                    )
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