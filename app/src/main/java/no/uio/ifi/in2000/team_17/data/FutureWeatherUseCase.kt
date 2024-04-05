package no.uio.ifi.in2000.team_17.data

import kotlinx.coroutines.flow.MutableStateFlow
import no.uio.ifi.in2000.team_17.model.WeatherPoint

//har tilgang til repository og returnerer use case data. F. eks temperatur
//henter data fra alle repo som trengs.
// a use case should have only one plublic function. All other functions have to be private utility functions. - single responsibility principle
class FutureWeatherUseCase(
    private val repository: Repository = RepositoryImplementation(),
) {
    suspend fun next24H(): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        return repository.getListOfWeatherPointsLists(2, 26)
    }

    suspend fun next48h(): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        return repository.getListOfWeatherPointsLists(27, 50)
    }

    suspend fun next3D(): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        return repository.getListOfWeatherPointsLists(51, 75)
    }

    suspend fun next4D(): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        return repository.getListOfWeatherPointsLists(76, 100)
    }

    suspend fun next5D(): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        return repository.getListOfWeatherPointsLists(101, 125)
    }

    suspend fun next6D(): MutableStateFlow<MutableList<MutableStateFlow<List<WeatherPoint>>>> {
        return repository.getListOfWeatherPointsLists(126, 150)
    }


}