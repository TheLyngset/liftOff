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
import no.uio.ifi.in2000.team_17.R

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