package no.uio.ifi.in2000.team_17.ui.theme.isobaricTestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IsobaricTestScreen (

) {
    val isobaricViewModel: IsobaricViewModel = viewModel()
    val uiState = isobaricViewModel.uiState.collectAsState()

    Row (
        Modifier
            .fillMaxSize()
    ){
        Column(Modifier.background(Color.Gray)) {
            Text(" Height ")
            uiState.value.layerHeights.forEach {
                Text(text = it.toString())
            }
        }
        Column(Modifier.background(Color.LightGray)) {
            Text(" Windspeed ")
            uiState.value.windSpeeds.forEach {
                Text(text = it.toString())
            }
        }
        Column(Modifier.background(Color.Gray)) {
            Text(" Wind direction ")
            uiState.value.windFromDirections.forEach {
                Text(text = it.toString())
            }
        }
        Column (Modifier.background(Color.LightGray)){
            Text(" temp ")
            uiState.value.temperatures.forEach {
                Text(text = it.toString())
            }
        }
        Column (Modifier.background(Color.Gray)){
            Text(" shear ")
            uiState.value.windShear.forEach {
                Text(text = it.toString())
            }
        }
    }
}