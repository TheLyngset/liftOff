package no.uio.ifi.in2000.team_17.ui

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import no.uio.ifi.in2000.team_17.App
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsScreen
import no.uio.ifi.in2000.team_17.ui.advanced_settings.AdvancedSettingsViewModel
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreen
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenViewModel
import no.uio.ifi.in2000.team_17.viewModelFactory
import java.lang.NumberFormatException


enum class Screen(val title: String, val logo: Int) {
    Home(title = "Home Screen", logo = R.drawable.logos),
    AdvancedSettings(title = "Advanced Settings", logo = R.drawable.logos)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    currentScreen: Screen,
    logoId: Int
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Each child of Row takes up equal space
                Image(
                    painter = painterResource(id = logoId),
                    contentDescription = "Logo",
                    modifier = Modifier

                        .padding(5.dp)
                        .size(70.dp)
                )
                Text(
                    text = "Oslo",
                    modifier = Modifier

                        .padding(10.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
                Image(
                    painter = painterResource(id = R.drawable.search_24px),
                    contentDescription = "Search",
                    modifier = Modifier
                        
                        .padding(5.dp)
                        .size(40.dp)
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White.copy(alpha = 0.65f)
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
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.7f))
            )
        },
        /*bottomBar = {
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
        }, */
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.name
        ){
            composable(route = Screen.Home.name){
                newHomeScreen(
                    Modifier
                        .padding(innerPadding)
                        .verticalScroll(scrollStateVertical))
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