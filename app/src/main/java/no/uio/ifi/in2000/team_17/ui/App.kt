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
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.App
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsScreen
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsViewModel
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreen
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenViewModel
import no.uio.ifi.in2000.team_17.viewModelFactory
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
    //Todo implemnt the new viewModelFactory for this viewModel
    val homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = viewModelFactory {
            HomeScreenViewModel(App.appModule.repository, App.appModule.settingsRepository, App.appModule.advancedSettingsRepository)
        }
    )
    val homeScreenUiState by homeScreenViewModel.homeScreenUiState.collectAsState()
    val scrollStateVertical = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val advancedSettingsViewModel = viewModel<AdvancedSettingsViewModel>(
        factory = viewModelFactory {
            AdvancedSettingsViewModel(App.appModule.advancedSettingsRepository)
        }
    )

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
                        .verticalScroll(scrollStateVertical), homeScreenUiState = homeScreenUiState,
                    toAdvancedSettings = {navController.navigate(Screen.AdvancedSettings.name)},
                    setMaxHeight = { try{homeScreenViewModel.setMaxHeight(it.toInt())} catch (e: NumberFormatException){ coroutineScope.launch{ snackbarHostState.showSnackbar("Invalid input") } } },
                    setLat = {try{homeScreenViewModel.setLat(it.toDouble())} catch (e: NumberFormatException){ coroutineScope.launch{ snackbarHostState.showSnackbar("Invalid input") } } },
                    setLng = {try{homeScreenViewModel.setLng(it.toDouble())} catch (e: NumberFormatException){ coroutineScope.launch{ snackbarHostState.showSnackbar("Invalid input") } } },
                    onLoad = { homeScreenViewModel.load() }
                )
            }
            composable(route = Screen.AdvancedSettings.name){
                AdvancedSettingsScreen(Modifier.padding(innerPadding),viewModel = advancedSettingsViewModel, Navigate = {navController.navigate(Screen.Home.name)}){
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