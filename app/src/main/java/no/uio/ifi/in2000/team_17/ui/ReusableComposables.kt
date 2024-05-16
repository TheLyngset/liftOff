package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.isUnspecified
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

@Composable
fun ConditionalText(modifier: Modifier = Modifier, text: String){

    val minimumLineLength = 1   //Change this to your desired value

    //Adding States
    var expandedState by remember { mutableStateOf(false) }
    var showReadMoreButtonState by remember { mutableStateOf(false) }
    val maxLines = if (expandedState) 200 else minimumLineLength

    Column(modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,                   //Make sure to add this line
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                if (textLayoutResult.lineCount > minimumLineLength-1) {           //Adding this check to avoid ArrayIndexOutOfBounds Exception
                    if (textLayoutResult.isLineEllipsized(minimumLineLength-1)) showReadMoreButtonState = true
                }
            }
        )
        if (showReadMoreButtonState) {
            Text(
                text = if (expandedState) "Read Less" else "Read More",
                color = MaterialTheme.colorScheme.secondary,

                modifier = Modifier.clickable {
                    expandedState = !expandedState
                },

                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                )

            )
        }

    }
}

@Composable
fun AutoHeightText(
    text: String,
    style: TextStyle = TextStyle(),
    modifier: Modifier = Modifier,
    color: Color = style.color
){
    var shouldDraw by remember { mutableStateOf(false)}
    var resizedTextStyle by remember{ mutableStateOf(style)}
    val defaultFontSize = MaterialTheme.typography.bodySmall.fontSize

    Text(text = text,
        style = resizedTextStyle,
        color = color,
        softWrap = false,
        modifier = modifier.drawWithContent { if(shouldDraw){ drawContent() } }
        ,
        onTextLayout = {result ->
            if(result.hasVisualOverflow){
                if(style.fontSize.isUnspecified){
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize*0.9
                )
            }
            else{
                shouldDraw = true
            }
        }
    )
}
