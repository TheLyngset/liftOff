package no.uio.ifi.in2000.team_17.data

import no.uio.ifi.in2000.team_17.model.WeatherPoint

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

    fun canLaunch(weatherPoint: WeatherPoint, maxWindSpeed: Double, maxShearWind: Double): Boolean {
        //tåke --- (connected to clouds, dew point and precipitation)
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
}

internal fun launchClearance(
    weatherPoint: WeatherPoint,
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