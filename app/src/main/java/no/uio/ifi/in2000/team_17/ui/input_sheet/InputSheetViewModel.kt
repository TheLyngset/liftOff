package no.uio.ifi.in2000.team_17.ui.input_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.settings.SettingsRepository


data class InputSheetUiState(
    val maxHeight: Int = 3,
    val latLng: LatLng = LatLng(59.0, 10.0)
)
class InputSheetViewModel(
    private val repository: Repository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val updateListener = repository.failedToUpdate.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )
    val uiState: StateFlow<InputSheetUiState> = settingsRepository.settingsFlow.map {
        InputSheetUiState(
            maxHeight = it.maxHeight,
            latLng = LatLng(it.lat, it.lng)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        InputSheetUiState()
    )
    fun setLat(lat:Double){
        viewModelScope.launch {
            settingsRepository.setLat(lat)
        }
    }
    fun setLng(lng:Double){
        viewModelScope.launch {
            settingsRepository.setLng(lng)
        }
    }
    fun setMaxHeight(height: Int){
        viewModelScope.launch{
            settingsRepository.setMaxHeight(height)
        }
    }
}