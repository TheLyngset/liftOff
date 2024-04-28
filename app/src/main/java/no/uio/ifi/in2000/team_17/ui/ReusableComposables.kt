package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.team17.Thresholds
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.model.Rain
import no.uio.ifi.in2000.team_17.model.WeatherParameter
import no.uio.ifi.in2000.team_17.model.WeatherPointInTime
import no.uio.ifi.in2000.team_17.model.WindLayer
import no.uio.ifi.in2000.team_17.model.WindShear
import no.uio.ifi.in2000.team_17.ui.home_screen.TrafficLightColor
import no.uio.ifi.in2000.team_17.usecases.WeatherUseCase

@Composable
fun IconSwitch(checked: Boolean, icon: ImageVector? = null, drawableON: Int = R.drawable.lock_locked, drawableOFF: Int = R.drawable.lock_locked, onFlip: () -> Unit) {
    Switch(
        checked = checked,
        thumbContent = {
            if(icon != null){
                Icon(
                    contentDescription = "Info",
                    imageVector = icon,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
            else if(checked){
                Icon(
                    contentDescription = null,
                    painter = painterResource(id = drawableON),
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            } else{
                Icon(
                    contentDescription = null,
                    painter = painterResource(id = drawableOFF),
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        },
        colors = SwitchDefaults.colors().copy(
            checkedTrackColor = MaterialTheme.colorScheme.primary,
        ),
        onCheckedChange = {
            onFlip()
        }
    )
}

fun calculateColor(type: WeatherParameter, value: String, thresholds: Thresholds): TrafficLightColor {
    return when(type){
        WeatherParameter.CLOUDFRACTION-> WeatherUseCase.canLaunch(WeatherPointInTime(cloudFraction = value.toDouble()), thresholds)
        WeatherParameter.GROUNDWIND -> WeatherUseCase.canLaunch(WeatherPointInTime(groundWind = WindLayer(value.toDouble())), thresholds)
        WeatherParameter.MAXWINDSHEAR -> WeatherUseCase.canLaunch(WeatherPointInTime(maxWindShear = WindShear(value.toDouble())), thresholds)
        WeatherParameter.MAXWIND -> WeatherUseCase.canLaunch(WeatherPointInTime(maxWind = WindLayer(value.toDouble())), thresholds)
        WeatherParameter.RAIN -> WeatherUseCase.canLaunch(WeatherPointInTime(rain = Rain(median = value.toDouble())), thresholds)
        WeatherParameter.HUMIDITY -> WeatherUseCase.canLaunch(WeatherPointInTime(humidity = value.toDouble()), thresholds)
        WeatherParameter.DEWPOINT -> WeatherUseCase.canLaunch(WeatherPointInTime(dewPoint = value.toDouble()), thresholds)
        WeatherParameter.FOG -> WeatherUseCase.canLaunch(WeatherPointInTime(fog = value.toDouble()), thresholds)
        WeatherParameter.DATE -> TrafficLightColor.WHITE
        WeatherParameter.TIME -> TrafficLightColor.WHITE
    }
}