package no.uio.ifi.in2000.team_17.ui.thresholds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsSerializer
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherParameter.*
import no.uio.ifi.in2000.team_17.ui.Background
import no.uio.ifi.in2000.team_17.ui.input_sheet.InputTextField
import no.uio.ifi.in2000.team_17.ui.Screen
import java.lang.NumberFormatException

@Composable
fun ThresholdsScreen(
    modifier: Modifier = Modifier,
    viewModel: ThresholdsViewModel,
    onCorrectInputFormat: (String) -> Unit,
    wrongInputFormat:(WeatherParameter)->Unit
){
    val uiState by viewModel.thresholdsUiState.collectAsState()
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
    var showInfo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { state.animateScrollTo(50) }
    Background()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            Modifier
                .padding(end = 34.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Screen.Thresholds.title,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 30.sp),
            )
            Icon(imageVector = Icons.Outlined.Info, contentDescription = null,modifier = Modifier.clickable {
                showInfo = true
            })
        }

        //Ground wind speed
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            InputTextField(
                value = groundWindSpeedText,
                onValueChange = { groundWindSpeedText = it },
                label = "Max ground wind speed",

            ) {
                val newValue = try {
                    it.toDouble()

                } catch (e: NumberFormatException) {
                    wrongInputFormat(GROUNDWIND)
                    uiState.groundWindSpeed
                }
                viewModel.setGroundWind(newValue)
                groundWindSpeedText = newValue.toString()
                onCorrectInputFormat("Max ground wind speed was set to $groundWindSpeedText m/s")
            }
            IconButton(onClick = {
                viewModel.reset(GROUNDWIND)
                groundWindSpeedText = defaults.groundWindSpeed.toString()
            }) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //Max wind speed
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = maxWindSpeedText,
                onValueChange = { maxWindSpeedText = it },
                label = "Max air wind speed",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(MAXWIND)
                    uiState.maxWindSpeed
                }
                viewModel.setMaxWind(newValue)
                maxWindSpeedText = newValue.toString()
                onCorrectInputFormat("Max air wind speed was set to $maxWindSpeedText m/s")
            }
            IconButton(onClick = {
                viewModel.reset(MAXWIND)
                maxWindSpeedText = defaults.maxWindSpeed.toString()
            }
            ) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max wind shear
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = maxWindShearText,
                onValueChange = { maxWindShearText = it },
                label = "Max wind shear",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(MAXWINDSHEAR)
                    uiState.maxWindShear
                }
                viewModel.setMaxWindShear(newValue)
                maxWindShearText = newValue.toString()
                onCorrectInputFormat("Max wind shear was set to $maxWindShearText m/s")
            }
            IconButton(onClick = {
                viewModel.reset(MAXWINDSHEAR)
                maxWindShearText = defaults.maxWindShear.toString()
            }) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max cloud fraction
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = cloudFractionText,
                onValueChange = { cloudFractionText = it },
                label = "Max cloud fraction in percent",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(CLOUDFRACTION)
                    uiState.cloudFraction
                }
                viewModel.setCloudFraction(newValue)
                cloudFractionText = newValue.toString()
                onCorrectInputFormat("Max cloud fraction was set to $cloudFractionText %")
            }
            IconButton(onClick = {viewModel.reset(CLOUDFRACTION)
                cloudFractionText = defaults.cloudFraction.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max fog
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = fogText,
                onValueChange = { fogText = it },
                label = "Max fog",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(FOG)
                    uiState.fog
                }
                viewModel.setFog(newValue)
                fogText = newValue.toString()
                onCorrectInputFormat("Max fog was set to $fogText")
            }
            IconButton(onClick = {viewModel.reset(FOG)
                fogText = defaults.fog.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max rain
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = rainText,
                onValueChange = { rainText = it },
                label = "Max rain probability",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(RAIN)
                    uiState.rain
                }
                viewModel.setRain(newValue)
                rainText = newValue.toString()
                onCorrectInputFormat("Max rain probability was set to $rainText %")
            }
            IconButton(onClick = {viewModel.reset(RAIN)
                rainText = defaults.rain.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max humidity
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = humidityText,
                onValueChange = { humidityText = it },
                label = "Max humidity",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(HUMIDITY)
                    uiState.humidity
                }
                viewModel.setHumiditiy(newValue)
                humidityText = newValue.toString()
                onCorrectInputFormat("Max humidity was set to $humidityText %")
            }
            IconButton(onClick = {viewModel.reset(HUMIDITY)
                humidityText = defaults.humidity.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max dewPoint
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = dewPointText,
                onValueChange = { dewPointText = it },
                label = "Max Dew Point",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(DEWPOINT)
                    uiState.dewPoint
                }
                viewModel.setDewPoint(newValue)
                dewPointText = newValue.toString()
                onCorrectInputFormat("Max Dew Point was set to $dewPointText")
            }
            IconButton(onClick = {viewModel.reset(DEWPOINT)
                dewPointText = defaults.dewPoint.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //margin
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            InputTextField(
                value = marginText,
                onValueChange = { marginText = it },
                label = "Safety margin",

            ) {
                val newValue = try {
                    it.toDouble()
                } catch (e: NumberFormatException) {
                    wrongInputFormat(MARGIN)
                    uiState.margin
                }
                viewModel.setMargin(newValue)
                marginText = newValue.toString()
                onCorrectInputFormat("Safety margin was set to $marginText")
            }
            IconButton(onClick = {viewModel.reset(MARGIN)
                marginText = defaults.margin.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                viewModel.reset(null)
                groundWindSpeedText = defaults.groundWindSpeed.toString()
                maxWindSpeedText = defaults.maxWindSpeed.toString()
                maxWindShearText = defaults.maxWindShear.toString()
                cloudFractionText = defaults.cloudFraction.toString()
                fogText = defaults.fog.toString()
                rainText = defaults.rain.toString()
                humidityText = defaults.humidity.toString()
                dewPointText = defaults.dewPoint.toString()
                marginText = defaults.margin.toString()
                onCorrectInputFormat("All thresholds were reset")

            }) {
                Text(text = "Reset All")
                Icon(Icons.Filled.Refresh, null)
            }
        }
    }
    ThresholdsInfo(modifier,showInfo) {
        showInfo = false
    }
}