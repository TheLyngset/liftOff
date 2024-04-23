package no.uio.ifi.in2000.team_17.ui

import android.graphics.fonts.FontStyle
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.type.content
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files.append
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenUiState
import org.w3c.dom.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSheet(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toThresholdsScreen: () -> Unit,
    setMaxHeight: (String) -> Unit,
    setLat: (String) -> Unit,
    setLng: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState : Boolean
) {
    if (sheetState) {
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            InputSheetContent(
                homeScreenUiState = homeScreenUiState,
                toAdvancedSettings = toThresholdsScreen,
                setMaxHeight = { setMaxHeight(it) },
                setLat = {setLat(it)},
                setLng = {setLng(it)},
                onDismiss = {
                    onDismiss()
                },
            )
        }
    }
}

@Composable
fun InputSheetContent(
    modifier: Modifier = Modifier,
    homeScreenUiState: HomeScreenUiState,
    toAdvancedSettings:()->Unit,
    setMaxHeight:(String) -> Unit,
    setLat:(String) -> Unit,
    setLng:(String) -> Unit,
    onDismiss:() -> Unit
) {
    var maxHeightText by remember { mutableStateOf(homeScreenUiState.maxHeight.toString()) }
    var latString by remember { mutableStateOf(homeScreenUiState.latLng.latitude.toString()) }
    var lngString by remember { mutableStateOf(homeScreenUiState.latLng.longitude.toString()) }
    Column(
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier.padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically) 
        {
            InputTextField(
                modifier = Modifier.fillMaxWidth(),
                value = maxHeightText,
                onValueChange = { maxHeightText = it },
                label = "Maximum height in km"
            ) { setMaxHeight(maxHeightText) }
        }
        Row(modifier.padding(horizontal = 15.dp), horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically) {
            InputTextField(
                value = latString,
                onValueChange = { latString = it },
                label = "Latitude",
                modifier = Modifier.weight(1f)
            ){ setLat(latString) }
            InputTextField(
                value = lngString,
                onValueChange = { lngString = it },
                label = "Longitude",
                modifier = Modifier.weight(1f)
            ){ setLng(lngString) }
        }
        ListItem(
            modifier = modifier.fillMaxHeight(1f).padding(top=15.dp),
            colors = ListItemDefaults.colors(MaterialTheme.colorScheme.primaryContainer),
            headlineContent = {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .padding(top = 7.dp),
                    fontWeight = FontWeight.SemiBold,
                    text = "Thresholds",
                    style = TextStyle(fontSize = 17.sp, color = Color.DarkGray)
                )
            },
            supportingContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ConditionalText(text = "When you can launch is determined by appropriate thresholds set specifically for the needs of the Kon-tiki project by Portal Space. If your rocket can launch in different conditions you can alter them by pressing the button under:")
                    Button(onClick = {toAdvancedSettings()}) {
                        Text(text = "Thresholds")
                    }
                }
            }
        )
    }
}

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onDone: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onDone(value)
            }
        ),
        modifier = modifier
    )
}




@Composable
fun ConditionalText(text: String){

    val minimumLineLength = 2   //Change this to your desired value

    //Adding States
    var expandedState by remember { mutableStateOf(false) }
    var showReadMoreButtonState by remember { mutableStateOf(false) }
    val maxLines = if (expandedState) 200 else minimumLineLength

    Column(modifier = Modifier.padding(start = 35.dp, end = 5.dp)) {
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
                color = Color.Gray,

                modifier = Modifier.clickable {
                    expandedState = !expandedState
                },

                style = MaterialTheme.typography.bodySmall

            )
        }

    }
}