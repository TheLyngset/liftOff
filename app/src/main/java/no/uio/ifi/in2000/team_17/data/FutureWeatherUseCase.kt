package no.uio.ifi.in2000.team_17.data

import no.uio.ifi.in2000.team_17.model.WeatherPointNew
import kotlin.properties.Delegates

//har tilgang til repository og returnerer use case data. F. eks temperatur
//henter data fra alle repo som trengs.
// a use case should have only one plublic function. All other functions have to be private utility functions. - single responsibility principle
class FutureWeatherUseCase(
    private val repository: Repository = RepositoryImplementation(),
) {
    // cca. 85-88 indexes in timeseries
    //needs to be tested

    var date: String by Delegates.observable("") { _, old, new ->
        if (old != new) {
            println("Variable has changed state")
        } else {
            println("Variable has not changed state")
        }
    }

    suspend fun next24H(): List<WeatherPointNew> {
        return repository.getListOfWeatherPoints(0, 23)
    }

    suspend fun next48h(): List<WeatherPointNew> {
        return repository.getListOfWeatherPoints(24, 47)
    }

    suspend fun next3D(): List<WeatherPointNew> {
        return repository.getListOfWeatherPoints(48, 71)
    }

    suspend fun next4D(): List<WeatherPointNew> {
        return repository.getListOfWeatherPoints(72, 75)
    }

    suspend fun next5D(): List<WeatherPointNew> {
        return repository.getListOfWeatherPoints(76, 83)
    }

    suspend fun next6D(): List<WeatherPointNew> {
        return repository.getListOfWeatherPoints(84, 88)
    }


}