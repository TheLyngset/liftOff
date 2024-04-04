package no.uio.ifi.in2000.team_17.data

import no.uio.ifi.in2000.team_17.model.WeatherPoint
import no.uio.ifi.in2000.team_17.model.weatherDTO.LocationforecastDTO

//har tilgang til repository og returnerer use case data. F. eks temperatur
//henter data fra alle repo som trengs.
// a use case should have only one plublic function. All other functions have to be private utility functions. - single responsibility principle
class FutureWeatherUseCase(private val repository: Repository) {
    suspend fun allData(): LocationforecastDTO {
        return repository.getLocationForecast()
    }

    suspend fun today(): WeatherPoint? {
        // TODO:
        return null
    }

    suspend fun day2(): WeatherPoint? {
        // TODO:
        return null
    }

    suspend fun day3(): WeatherPoint? {
        // TODO:
        return null
    }


}