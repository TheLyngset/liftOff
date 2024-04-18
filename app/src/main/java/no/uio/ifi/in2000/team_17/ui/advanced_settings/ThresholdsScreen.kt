package no.uio.ifi.in2000.team_17.ui.advanced_settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.InputTextField
import no.uio.ifi.in2000.team_17.ui.Screen
import java.lang.NumberFormatException

@Composable
fun ThresholdsScreen(
    modifier: Modifier = Modifier,
    viewModel: ThresholdsViewModel,
    wrongInputFormat:()->Unit
){
    val uiState = viewModel.thresholdsUiState.collectAsState().value
    //strings for inputfields
    var groundWindSpeedText by remember { mutableStateOf("")}
    var maxWindSpeedText by remember { mutableStateOf("")}
    var maxWindShearText by remember { mutableStateOf("")}
    var cloudFractionText by remember { mutableStateOf("")}
    var fogText by remember { mutableStateOf("")}
    var rainText by remember { mutableStateOf("")}
    var humidityText by remember { mutableStateOf("")}
    var dewPointText by remember { mutableStateOf("")}
    var marginText by remember { mutableStateOf("")}

    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(50) }

    Box(modifier = Modifier.fillMaxSize() ){
        Image(painter = painterResource(id = R.drawable.sky),
            contentDescription = null, contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(
                    scaleX = 2.4f,
                    scaleY = 1.4f,
                    translationX = 100f,
                    translationY = 150f
                ),
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(0.75f))){

        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = Screen.Thresholds.title,
            fontWeight = FontWeight.Bold,
            style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
        )

        //Ground wind speed
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically) {
            InputTextField(
                value = groundWindSpeedText,
                onValueChange = { groundWindSpeedText = it },
                label = "Max ground wind speed"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.groundWindSpeed
                }
                viewModel.setGroundWind(newValue)
                groundWindSpeedText = newValue.toString()
            }
            Text(uiState.groundWindSpeed.toString())
        }

        //Max wind speed
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = maxWindSpeedText,
                onValueChange = { maxWindSpeedText = it },
                label = "Max air wind speed"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.maxWindSpeed
                }
                viewModel.setMaxWind(newValue)
                maxWindSpeedText = newValue.toString()
            }
            Text(uiState.maxWindSpeed.toString())
        }

        //max wind shear
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = maxWindShearText,
                onValueChange = { maxWindShearText = it },
                label = "Max wind shear"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.maxWindShear
                }
                viewModel.setMaxWindShear(newValue)
                maxWindShearText = newValue.toString()
            }
            Text(uiState.maxWindShear.toString())
        }

        //max cloud fraction
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = cloudFractionText,
                onValueChange = { cloudFractionText = it },
                label = "Max cloud fraction in percent"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.cloudFraction
                }
                viewModel.setCloudFraction(newValue)
                cloudFractionText = newValue.toString()
            }
            Text(uiState.cloudFraction.toString())
        }

        //max fog
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = fogText,
                onValueChange = { fogText = it },
                label = "Max fog"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.fog
                }
                viewModel.setFog(newValue)
                fogText = newValue.toString()
            }
            Text(uiState.fog.toString())
        }

        //max rain
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = rainText,
                onValueChange = { rainText = it },
                label = "Max allowed rain in mm"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.rain
                }
                viewModel.setRain(newValue)
                rainText = newValue.toString()
            }
            Text(uiState.rain.toString())
        }

        //max humidity
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = humidityText,
                onValueChange = { humidityText = it },
                label = "Max humidity"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.humidity
                }
                viewModel.setHumiditiy(newValue)
                humidityText = newValue.toString()
            }
            Text(uiState.humidity.toString())
        }

        //max dewPoint
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = dewPointText,
                onValueChange = { dewPointText = it },
                label = "Highest dew point allowed"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.dewPoint
                }
                viewModel.setDewPoint(newValue)
                dewPointText = newValue.toString()
            }
            Text(uiState.dewPoint.toString())
        }

        //margin
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = marginText,
                onValueChange = { marginText = it },
                label = "Safety margin"
            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat()
                    uiState.margin
                }
                viewModel.setMargin(newValue)
                marginText = newValue.toString()
            }
            Text(uiState.margin.toString())
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                viewModel.reset()
                groundWindSpeedText = ""
                maxWindSpeedText = ""
                maxWindShearText = ""
                cloudFractionText = ""
                fogText = ""
                rainText = ""
                humidityText = ""
                dewPointText = ""
                marginText = ""

            }) {
                Text(text = "Reset Advanced Settings")
            }
        }

    }
}