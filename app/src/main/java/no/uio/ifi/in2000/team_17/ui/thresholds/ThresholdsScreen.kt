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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.team_17.R
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
    wrongInputFormat:(WeatherParameter)->Unit,
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
                label = stringResource(R.string.max_ground_wind_speed_label)
            ) {
                try {
                    viewModel.setGroundWind(it.toDouble())
                    onCorrectInputFormat("Max ground wind speed was set to $groundWindSpeedText m/s")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(GROUNDWIND)
                }
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
                label = stringResource(R.string.max_air_wind_speed_label)
            ) {
                try {
                    viewModel.setMaxWind(it.toDouble())
                    onCorrectInputFormat("Max air wind speed was set to $maxWindSpeedText m/s")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(MAXWIND)
                }
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
                label = stringResource(id = R.string.max_shear_wind)
            ) {
                try {
                    viewModel.setMaxWindShear(it.toDouble())
                    onCorrectInputFormat("Max wind shear was set to $maxWindShearText m/s")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(MAXWINDSHEAR)
                }
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
                label = stringResource(R.string.max_cloud_fraction_in_percent)
            ) {
                try {
                    viewModel.setCloudFraction(it.toDouble())
                    onCorrectInputFormat("Max cloud fraction was set to $cloudFractionText m/s")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(CLOUDFRACTION)
                }
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
                label = stringResource(id = R.string.max_fog_title)
            ) {
                try {
                    viewModel.setFog(it.toDouble())
                    onCorrectInputFormat("Max fog fraction was set to $fogText %")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(FOG)
                }
            }
            IconButton(onClick = {viewModel.reset(FOG)
                fogText = defaults.fog.toString()}) {
                Icon(Icons.Filled.Refresh, "Reset fog")
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
                label = stringResource(R.string.max_rain_probability)
            ) {
                try {
                    viewModel.setRain(it.toDouble())
                    onCorrectInputFormat("Max probability of rain was set to $rainText %")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(RAIN)
                }
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
                label = stringResource(id = R.string.max_humidity_title)
            ) {
                try {
                    viewModel.setHumiditiy(it.toDouble())
                    onCorrectInputFormat("Max humidity was set to $humidityText %")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(HUMIDITY)
                }
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
                label = stringResource(id = R.string.max_dew_point_title)
            ) {
                try {
                    viewModel.setDewPoint(it.toDouble())
                    onCorrectInputFormat("Max Dew Point was set to $dewPointText °")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(DEWPOINT)
                }
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
                label = stringResource(id = R.string.safety_margin_title)
            ) {
                try {
                    viewModel.setMargin(it.toDouble())
                    onCorrectInputFormat("Safety margin was set to $marginText")
                } catch (e: NumberFormatException) {
                    wrongInputFormat(MARGIN)
                }
            }
            IconButton(onClick = {viewModel.reset(MARGIN)
                marginText = defaults.margin.toString()}) {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                viewModel.resetAll()
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
                Text(text = stringResource(R.string.reset_all))
                Icon(Icons.Filled.Refresh, null)
            }
        }
    }
    ThresholdsInfo(modifier,showInfo) {
        showInfo = false
    }
}