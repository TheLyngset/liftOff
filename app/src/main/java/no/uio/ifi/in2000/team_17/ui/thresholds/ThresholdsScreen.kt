package no.uio.ifi.in2000.team_17.ui.thresholds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
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
fun TitleRow(
    showInfo:()-> Unit,
    resetAll:()-> Unit
) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = Screen.Thresholds.title,
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 30.sp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                resetAll()
            }) {
            Text(text = stringResource(R.string.reset_all))
            Icon(Icons.Filled.Refresh, null)
        }

        Icon(imageVector = Icons.Outlined.Info, contentDescription = "Info about thresholds",modifier = Modifier
            .weight(0.4f)
            .clickable {
                showInfo()
            }
        )
    }
}

@Composable
fun InputFieldColumn1(
    fraction: Float,
    marginText: String,
    groundWindSpeedText: String,
    maxWindSpeedText:String,
    maxWindShearText:String,
    cloudFractionText:String,
    update:(WeatherParameter, String) -> Unit,
    set:(WeatherParameter, String)->Unit,
    reset:(WeatherParameter)->Unit
) {
    //margin
    Column(
        modifier = Modifier.fillMaxWidth(fraction) ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = marginText,
                onValueChange = { update(MARGIN, it) },
                label = stringResource(id = R.string.safety_margin_title)
            ) { set(MARGIN, it) }
            IconButton(onClick = {
                reset(MARGIN)
            }) {
                Icon(Icons.Filled.Refresh, "reset margin")
            }
        }

        //Ground wind speed
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = groundWindSpeedText,
                onValueChange = { update(GROUNDWIND, it) },
                label = stringResource(R.string.max_ground_wind_speed_label)
            ) {
                set(GROUNDWIND, it)
            }
            IconButton(onClick = {
                reset(GROUNDWIND)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = maxWindSpeedText,
                onValueChange = { update(MAXWIND, it) },
                label = stringResource(R.string.max_air_wind_speed_label)
            ) {
                set(MAXWIND, it)
            }
            IconButton(onClick = { reset(MAXWIND) })
            {
                Icon(Icons.Filled.Refresh, null)
            }
        }

        //max wind shear
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = maxWindShearText,
                onValueChange = { update(MAXWINDSHEAR, it) },
                label = stringResource(id = R.string.max_shear_wind)
            ) {
                set(MAXWINDSHEAR, it)
            }
            IconButton(onClick = {
                reset(MAXWINDSHEAR)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = cloudFractionText,
                onValueChange = { update(CLOUDFRACTION, it) },
                label = stringResource(R.string.max_cloud_fraction_in_percent)
            ) {
                set(CLOUDFRACTION, it)
            }
            IconButton(onClick = { reset(CLOUDFRACTION) })
            {
                Icon(Icons.Filled.Refresh, null)
            }
        }
    }
}


@Composable
fun InputFieldColumn2(
    fraction: Float,
    fogText: String,
    rainText: String,
    humidityText:String,
    dewPointText:String,
    update:(WeatherParameter, String) -> Unit,
    set:(WeatherParameter, String)->Unit,
    reset:(WeatherParameter)->Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(fraction),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //margin
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = fogText,
                onValueChange = { update(FOG, it) },
                label = stringResource(id = R.string.max_fog_title)
            ) { set(FOG, it) }
            IconButton(onClick = {
                reset(FOG)
            }) {
                Icon(Icons.Filled.Refresh, "reset fog")
            }
        }

        //Ground wind speed
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = rainText,
                onValueChange = { update(RAIN, it) },
                label = stringResource(R.string.max_allowed_rain_title)
            ) {
                set(RAIN, it)
            }
            IconButton(onClick = {
                reset(RAIN)
            }) {
                Icon(Icons.Filled.Refresh, "reset rain")
            }
        }

        //Max wind speed
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = humidityText,
                onValueChange = { update(HUMIDITY, it) },
                label = stringResource(R.string.max_humidity_title)
            ) {
                set(HUMIDITY, it)
            }
            IconButton(onClick = { reset(HUMIDITY) })
            {
                Icon(Icons.Filled.Refresh, "reset humidity")
            }
        }

        //max wind shear
        Row(
            Modifier
                .padding(end = 21.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputTextField(
                value = dewPointText,
                onValueChange = { update(DEWPOINT, it) },
                label = stringResource(id = R.string.max_shear_wind)
            ) {
                set(DEWPOINT, it)
            }
            IconButton(onClick = {
                reset(DEWPOINT)
            }) {
                Icon(Icons.Filled.Refresh, "reset dew point")
            }
        }
    }
}
@Composable
fun ThresholdsScreen(
    modifier: Modifier = Modifier,
    windowHeightSizeClass: WindowHeightSizeClass,
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

    fun update(type: WeatherParameter, text: String, set: Boolean = false){
        when(type){
            GROUNDWIND -> groundWindSpeedText = text
            MAXWINDSHEAR -> maxWindShearText = text
            MAXWIND -> maxWindSpeedText = text
            CLOUDFRACTION -> cloudFractionText = text
            RAIN -> rainText = text
            HUMIDITY -> humidityText = text
            DEWPOINT -> dewPointText = text
            FOG -> fogText = text
            MARGIN -> marginText = text
            else -> {}
        }
        if(set){
            try {
                when (type) {
                    GROUNDWIND -> {
                        viewModel.setGroundWind(text.toDouble())
                        onCorrectInputFormat("Max ground wind was set to $groundWindSpeedText m/s")
                    }
                    MAXWINDSHEAR -> {
                        viewModel.setMaxWindShear(text.toDouble())
                        onCorrectInputFormat("Max wind shear was set to $maxWindShearText m/s")
                    }
                    MAXWIND -> {
                        viewModel.setMaxWind(text.toDouble())
                        onCorrectInputFormat("Max air wind was set to $maxWindSpeedText m/s")
                    }
                    CLOUDFRACTION -> {
                        viewModel.setCloudFraction(text.toDouble())
                        onCorrectInputFormat("Max cloud fraction was set to $cloudFractionText %")
                    }
                    RAIN -> {
                        viewModel.setRain(text.toDouble())
                        onCorrectInputFormat("Max probability of rain was set to $rainText %")
                    }
                    HUMIDITY -> {
                        viewModel.setHumiditiy(text.toDouble())
                        onCorrectInputFormat("Max humidity was set to $humidityText %")
                    }
                    DEWPOINT -> {
                        viewModel.setDewPoint(text.toDouble())
                        onCorrectInputFormat("Max dew point was set to $dewPointText °")
                    }
                    FOG -> {
                        viewModel.setFog(text.toDouble())
                        onCorrectInputFormat("Max fog fraction was set to $fogText %")
                    }
                    MARGIN -> {
                        viewModel.setMargin(text.toDouble())
                        onCorrectInputFormat("Margin was set to $marginText")
                    }
                    else -> {}
                }

            } catch (e: NumberFormatException) {
                when (type) {
                    GROUNDWIND -> wrongInputFormat(GROUNDWIND)
                    MAXWINDSHEAR -> wrongInputFormat(MAXWINDSHEAR)
                    MAXWIND -> wrongInputFormat(MAXWIND)
                    CLOUDFRACTION -> wrongInputFormat(CLOUDFRACTION)
                    RAIN -> wrongInputFormat(RAIN)
                    HUMIDITY -> wrongInputFormat(HUMIDITY)
                    DEWPOINT -> wrongInputFormat(DEWPOINT)
                    FOG -> wrongInputFormat(FOG)
                    MARGIN -> wrongInputFormat(MARGIN)
                    else -> {}
                }
            }
        }
    }

    Background()
    if(windowHeightSizeClass != WindowHeightSizeClass.Compact){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(state),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TitleRow(
                showInfo = { showInfo = true }
            ) {
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
            }

            InputFieldColumn1(
                fraction = 1f,
                marginText = marginText,
                groundWindSpeedText = groundWindSpeedText,
                maxWindSpeedText = maxWindSpeedText,
                maxWindShearText = maxWindShearText,
                cloudFractionText = cloudFractionText,
                update = { type, text -> update(type, text) },
                set = { type, text -> update(type, text, set = true) },
                reset = { viewModel.reset(it) }
            )
            InputFieldColumn2(
                fraction = 1f,
                fogText = fogText,
                rainText = rainText,
                humidityText = humidityText,
                dewPointText = dewPointText,
                update = {type, text ->  update(type, text) },
                set = { type, text ->  update(type, text, set = true) },
                reset = { viewModel.reset(it) }
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
    else{
        Column(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .verticalScroll(state),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TitleRow(
                showInfo = { showInfo = true }
            ) {
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
            }
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                InputFieldColumn1(
                    fraction = 0.5f,
                    marginText = marginText,
                    groundWindSpeedText = groundWindSpeedText,
                    maxWindSpeedText = maxWindSpeedText,
                    maxWindShearText = maxWindShearText,
                    cloudFractionText = cloudFractionText,
                    update = { type, text -> update(type, text) },
                    set = { type, text -> update(type, text, set = true) },
                    reset = { viewModel.reset(it) }
                )
                InputFieldColumn2(
                    fraction = 1f,
                    fogText = fogText,
                    rainText = rainText,
                    humidityText = humidityText,
                    dewPointText = dewPointText,
                    update = { type, text -> update(type, text) },
                    set = { type, text -> update(type, text, set = true) },
                    reset = { viewModel.reset(it) }
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    ThresholdsInfo(modifier,showInfo) {
        showInfo = false
    }
}

