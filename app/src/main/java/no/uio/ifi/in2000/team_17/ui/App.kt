package no.uio.ifi.in2000.team_17.ui

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.R
import java.lang.NumberFormatException


enum class Screen(val title: String, val logo: Int) {
    Home(title = "Home Screen", logo = R.drawable.logor),
    Input(title = "Input Screen", logo = R.drawable.logor)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    currentScreen: Screen,
    logoId: Int
 ){
    TopAppBar(
        title = { Image(painter = painterResource(id = logoId), contentDescription = "Test") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun App(
    navController:NavHostController = rememberNavController(),

){
    val uiViewModel: UiViewModel = viewModel()
    val uiState by uiViewModel.uiState.collectAsState()
    val scrollStateVertical = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = Screen.Home,
                logoId = Screen.Home.logo,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                    Button(onClick = { navController.navigate(Screen.Input.name) }) {
                        Text(Screen.Input.title)
                    }
                    Button(onClick = {navController.navigate(Screen.Home.name)}) {
                        Text(Screen.Home.title)
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.name
        ){
            composable(route = Screen.Home.name){
                HomeScreen(
                    Modifier
                        .padding(innerPadding)
                        .verticalScroll(scrollStateVertical),uiState = uiState)
            }
            composable(route = Screen.Input.name){
                InputScreen(Modifier.padding(innerPadding), uiState){latLngString, maxHeightString ->
                    try {
                        val latLng = LatLng(latLngString.split(", ")[0].toDouble(),latLngString.split(", ")[1].toDouble())
                        val maxHeight = maxHeightString.toInt()
                        uiViewModel.load(latLng, maxHeight)
                        navController.navigate(Screen.Home.name)
                    }catch (e:NumberFormatException){
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Invalid input"
                            )
                        }
                    }
                }
            }
        }
    }
}