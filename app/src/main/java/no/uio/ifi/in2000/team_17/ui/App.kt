package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsScreen
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsViewModel
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreen
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenViewModel
import java.lang.NumberFormatException


enum class Screen(val title: String, val logo: Int) {
    Home(title = "Home Screen", logo = R.drawable.logor),
    AdvancedSettings(title = "Advanced Settings", logo = R.drawable.logor)
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
    //Using viewModel Factories to take the repository created in Main activity as a parameter
    val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
    val homeScreenUiState by homeScreenViewModel.homeScreenUiState.collectAsState()
    val scrollStateVertical = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val advancedSettingsViewModel: AdvancedSettingsViewModel = viewModel(factory = AdvancedSettingsViewModel.Factory)
    val advancedSettingsUiState by advancedSettingsViewModel.advancedSettingsUiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = Screen.Home,
                logoId = Screen.Home.logo,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.name
        ){
            composable(route = Screen.Home.name) {
                HomeScreen(
                    Modifier
                        .padding(innerPadding)
                        .verticalScroll(scrollStateVertical), homeScreenUiState = homeScreenUiState
                )
                { latLngString, maxHeightString ->
                    try {
                        val latLng = LatLng(
                            latLngString.split(", ")[0].toDouble(),
                            latLngString.split(", ")[1].toDouble()
                        )
                        val maxHeight = maxHeightString.toInt()
                        homeScreenViewModel.load(latLng, maxHeight)
                    } catch (e: NumberFormatException) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Invalid input"
                            )
                        }
                    }
                }
            }
            composable(route = Screen.AdvancedSettings.name) {
                AdvancedSettingsScreen(Modifier.padding(innerPadding), uiState = advancedSettingsUiState)
            }
        }
    }
}