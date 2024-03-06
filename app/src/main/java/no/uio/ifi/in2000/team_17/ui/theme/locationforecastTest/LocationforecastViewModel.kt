package no.uio.ifi.in2000.team_17.ui.theme.locationforecastTest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.Locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.team_17.data.Locationforecast.LocationForecastRepositoryImplementation
import no.uio.ifi.in2000.team_17.data.Locationforecast.LocationforecastAllData

data class LocationforecastUiState(
    val locationforecastData: LocationforecastAllData = LocationforecastAllData(null, null, null),
)
class LocationforecastViewModel : ViewModel() {
    // Create instance of the repository that fetches data.
    private val locationforecastRepository: LocationForecastRepository = LocationForecastRepositoryImplementation()

    // Private mutable state flow to represent the UI state.
    private val _locationforecastUiState = MutableStateFlow(LocationforecastUiState())

    // Public immutable state flow to expose the UI state to the Screen.
    val locationforecastUiState: StateFlow<LocationforecastUiState> = _locationforecastUiState.asStateFlow()

    init {
        Log.d("LOCATIONFORECAST_VIEW_MODEL", "Calling loadJokes()")
        loadLocationforecastData()
    }
    private fun loadLocationforecastData() {
        // Do an asynchronous operation to fetch jokes on another thread (Dispatchers.IO)
        viewModelScope.launch(Dispatchers.IO) {
            // Update the private mutableStateFlow with the loaded list of jokes
            _locationforecastUiState.update { currentLocationforecastUiState ->

                Log.d("LOCFORECAST_VIEW_MODEL", "Calling getLocationforecastData()")
                // Get data from the repository (which then gets them from the data source)
                val locationforecastData = locationforecastRepository.getLocationforecastData()

                Log.d("LOCATIONFORECAST_VIEW_MODEL", "Updating _locationforecastUiState")
                // and replace the current mutableStateFlow
                currentLocationforecastUiState.copy(locationforecastData = locationforecastData)
            }
        }
    }
}