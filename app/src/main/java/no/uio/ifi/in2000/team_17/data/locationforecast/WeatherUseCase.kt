package no.uio.ifi.in2000.team_17.data.locationforecast

import kotlinx.coroutines.flow.first
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.WeatherPoint
import kotlin.math.round

//har tilgang til repository og returnerer use case data. F. eks temperatur
//henter data fra alle repo som trengs.
class WeatherUseCase() {

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

  fun canLaunch(weatherPoint: WeatherPoint, maxWindHeigth: Double, maxShearWind: Double): Boolean{
        //mangler:
        // max shear wind
        // max wind in air
        //tåke --- (connected to clouds, dew point and precipitation)
  return(
          weatherPoint.windSpeed < 8.6 &&
          weatherPoint.humidity < 75 &&
          weatherPoint.dewPoint < 15 &&
          weatherPoint.cloudFraction < 15 &&
          weatherPoint.rain < 1 &&
          weatherPoint.windShear < 24.5 &&
          maxWindHeigth < 17.2 &&
          maxShearWind < 24.5)
    }
}
internal fun launchClearance(weatherPoint: WeatherPoint, maxWindHeigth: Int, maxShearWind: Double): Boolean{
 return   return(
         weatherPoint.windSpeed < 8.6 &&
                 weatherPoint.humidity < 75 &&
                 weatherPoint.dewPoint < 15 &&
                 weatherPoint.cloudFraction < 15 &&
                 weatherPoint.rain < 1 &&
                 weatherPoint.windShear < 24.5 &&
                 maxWindHeigth < 17.2 &&
                 maxShearWind < 24.5)
}