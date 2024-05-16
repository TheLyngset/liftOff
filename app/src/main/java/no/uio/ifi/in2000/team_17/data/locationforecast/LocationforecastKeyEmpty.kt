package no.uio.ifi.in2000.team_17.data.locationforecast
/**
 * This class is used to store the API key for the locationforecast service.
 * The API key is stored in a separate file to avoid accidentally committing it to the repository.
 * Please duplicate this file and rename it to LocationforecastKey.kt,
 * then paste your API key in the apiKey variable.
 */
final class LocationforecastKeyEmpty {
    private val apiKey = "" // paste your api key here
    fun getAPIKey(): String{
        return apiKey
    }
}