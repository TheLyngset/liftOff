package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DataScreen(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState
){
    Row(
        modifier.fillMaxSize(),
    ) {
        for(index:Int in 0..5){
            val weatherPointInTime = dataScreenUiState.weatherDataLists.get(index)
            Column {
                Text(text = weatherPointInTime.time)
                Text(text = weatherPointInTime.groundWind.speed.toString())
            }
        }
    }
}