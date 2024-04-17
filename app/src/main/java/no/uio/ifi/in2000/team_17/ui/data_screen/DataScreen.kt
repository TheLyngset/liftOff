package no.uio.ifi.in2000.team_17.ui.data_screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DataScreen(
    modifier: Modifier = Modifier,
    dataScreenUiState: DataScreenUiState
){
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(50) }


        Column {


            Row(
                modifier


            ) {
                Column(modifier.padding(3.dp)) {
                    Text(text = "Time:")
                    Text(text = "GroundWind:")
                    Text("Max Wind Shear:")
                    Text("Cloud fraction:")
                    Text("Median rain:")
                    Text("Humidity:")
                    Text("Dew point")
                    Text("Fog fraction")
                }
                Row(
                    Modifier

                        .horizontalScroll(state)
                ) {
                    for (index: Int in 0..5) {
                        val weatherPointInTime = dataScreenUiState.weatherDataLists.get(index)
                        Column(modifier.padding(3.dp)) {
                            Text(text = weatherPointInTime.time)
                            Text(text = weatherPointInTime.groundWind.speed.toString())
                            Text(weatherPointInTime.maxWindShear.speed.toString())
                            Text(weatherPointInTime.cloudFraction.toString())
                            Text(weatherPointInTime.rain.median.toString())
                            Text(weatherPointInTime.humidity.toString())
                            Text(weatherPointInTime.dewPoint.toString())
                            Text(weatherPointInTime.fog.toString())
                        }
                    }
                }
            }
            Spacer(Modifier.size(20.dp))
            Row(
                Modifier

            ) {

                pre()

            }
        }

}