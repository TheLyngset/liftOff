package no.uio.ifi.in2000.team_17.ui.thresholds

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.ui.BackGroundImage
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
    var groundWindSpeedText by remember { mutableStateOf(uiState.groundWindSpeed.toString())}
    var maxWindSpeedText by remember { mutableStateOf(uiState.maxWindSpeed.toString())}
    var maxWindShearText by remember { mutableStateOf(uiState.maxWindShear.toString())}
    var cloudFractionText by remember { mutableStateOf(uiState.cloudFraction.toString())}
    var fogText by remember { mutableStateOf(uiState.fog.toString())}
    var rainText by remember { mutableStateOf(uiState.rain.toString())}
    var humidityText by remember { mutableStateOf(uiState.humidity.toString())}
    var dewPointText by remember { mutableStateOf(uiState.dewPoint.toString())}
    var marginText by remember { mutableStateOf(uiState.margin.toString())}

    val state = rememberScrollState()
    val defaults = ThresholdsSerializer.defaultValue
    LaunchedEffect(Unit) { state.animateScrollTo(50) }

    BackGroundImage(alpha = 0.75f)
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
            style = TextStyle(fontSize = 30.sp),
        )

        //Ground wind speed
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
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
            IconButton(onClick = {
                viewModel.reset("groundWind")
                groundWindSpeedText = defaults.groundWindSpeed.toString()
            }) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {
                viewModel.reset("maxWind")
                maxWindSpeedText = defaults.maxWindSpeed.toString()
            }
            ) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {
                viewModel.reset("maxShear")
                maxWindShearText = defaults.maxWindShear.toString()
            }) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {viewModel.reset("cloud")
                cloudFractionText = defaults.cloudFraction.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {viewModel.reset("fog")
                fogText = defaults.fog.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {viewModel.reset("rain")
                rainText = defaults.rain.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {viewModel.reset("humidity")
                humidityText = defaults.humidity.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {viewModel.reset("dewPoint")
                dewPointText = defaults.dewPoint.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
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
            IconButton(onClick = {viewModel.reset("margin")
                marginText = defaults.margin.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                viewModel.reset()
                groundWindSpeedText = defaults.groundWindSpeed.toString()
                maxWindSpeedText = defaults.maxWindSpeed.toString()
                maxWindShearText = defaults.maxWindShear.toString()
                cloudFractionText = defaults.cloudFraction.toString()
                fogText = defaults.fog.toString()
                rainText = defaults.rain.toString()
                humidityText = defaults.humidity.toString()
                dewPointText = defaults.dewPoint.toString()
                marginText = defaults.margin.toString()

            }) {
                Text(text = "Reset All")
                Icon(Icons.Filled.Refresh, null)
            }
        }
    }
}