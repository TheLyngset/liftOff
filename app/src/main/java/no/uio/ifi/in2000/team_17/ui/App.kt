package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.App
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.data_screen.DataScreen
import no.uio.ifi.in2000.team_17.ui.data_screen.DataScreenViewModel
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreen
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenViewModel
import no.uio.ifi.in2000.team_17.ui.judicial_screen.JudicialScreen
import no.uio.ifi.in2000.team_17.ui.thresholds.ThresholdsScreen
import no.uio.ifi.in2000.team_17.ui.thresholds.ThresholdsViewModel
import no.uio.ifi.in2000.team_17.viewModelFactory


enum class Screen(val title: String, val logo: Int) {
    Home(title = "Home Screen", logo = R.drawable.logoicon),
    Thresholds(title = "Thresholds", logo = R.drawable.logor),
    Data(title = "Data Screen", logo = R.drawable.logor),
    Judicial(title = "Judicial Screen", logo = R.drawable.logor),
    TechnicalDetailsScreen(title = "Technical Details", logo = R.drawable.logor),
    Empty("", 0)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    logoId: Int,
    onSearchClick: () -> Unit,
    onLogoClick: () -> Unit,
    //modifier: Modifier
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = logoId),
                    contentDescription = "Logo",
                    modifier = Modifier.clickable { onLogoClick() }
                )

                Text(
                    text = "Oslo",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.search_24px),
                    contentDescription = "Search",
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .clickable {
                            onSearchClick()
                        }
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White.copy(alpha = 0.65f)
            //MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun App(
    navController: NavHostController = rememberNavController(),

    ) {
    //Using viewModel Factories to take the repository created in Main activity as a parameter
    val homeScreenViewModel: HomeScreenViewModel = viewModel(
        factory = viewModelFactory {
            HomeScreenViewModel(
                App.appModule.repository,
                App.appModule.settingsRepository,
                App.appModule.thresholdsRepository
            )
        }
    )

    val homeScreenUiState by homeScreenViewModel.homeScreenUiState.collectAsState()
    val scrollStateVertical = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val thresholdsViewModel = viewModel<ThresholdsViewModel>(
        factory = viewModelFactory {
            ThresholdsViewModel(App.appModule.thresholdsRepository)
        }
    )
    val dataScreenViewModel = viewModel<DataScreenViewModel>(
        factory = viewModelFactory {
            DataScreenViewModel(
                App.appModule.repository,
                App.appModule.settingsRepository,
                App.appModule.thresholdsRepository
            )
        }
    )
    val dataScreenUiState by dataScreenViewModel.dataScreenUiState.collectAsState()

    var sheetState by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.paint(painterResource(id = R.drawable.sky)),
        topBar = {
            AppTopBar(
                logoId = Screen.Home.logo,
                onSearchClick = { sheetState = true },
                onLogoClick = { navController.navigate("Home") },
                //Modifier.shadow(elevation = 15.dp, spotColor = Color.DarkGray, shape = RoundedCornerShape(1.dp))
            )
        },
        bottomBar = {
            BottomBar(modifier = Modifier.fillMaxWidth()) {
                when (it) {
                    0 -> navController.navigate(Screen.Home.name)
                    1 -> navController.navigate(Screen.Data.name)
                    2 -> navController.navigate(Screen.Judicial.name)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.name
        ) {
            composable(route = Screen.Home.name) {
                HomeScreen(Modifier.padding(innerPadding), homeScreenUiState)
            }
            composable(route = Screen.Thresholds.name) {
                ThresholdsScreen(
                    Modifier.padding(innerPadding),
                    viewModel = thresholdsViewModel
                ) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Invalid input"
                        )
                    }
                }
            }
            composable(route = Screen.Data.name) {
                DataScreen(
                    modifier = Modifier.padding(innerPadding),
                    dataScreenUiState
                ) { dataScreenViewModel.setTimeIndex(it) }
            }
            composable(route = Screen.Judicial.name) {
                JudicialScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }

    InputSheet(
        homeScreenUiState = homeScreenUiState,
        toThresholdsScreen = {
            navController.navigate(Screen.Thresholds.name)
            sheetState = false
        },
        setMaxHeight = {
            try {
                homeScreenViewModel.setMaxHeight(it.toInt())
            } catch (e: NumberFormatException) {
                coroutineScope.launch { snackbarHostState.showSnackbar("Invalid Max Height") }
            }
        },
        setLat = {
            try {
                homeScreenViewModel.setLat(it.toDouble())
            } catch (e: NumberFormatException) {
                coroutineScope.launch { snackbarHostState.showSnackbar("Invalid Latitude") }
            }
        },
        setLng = {
            try {
                homeScreenViewModel.setLng(it.toDouble())
            } catch (e: NumberFormatException) {
                coroutineScope.launch { snackbarHostState.showSnackbar("Invalid Longitude") }
            }
        },
        sheetState = sheetState,
        onDismiss = { sheetState = false }
    )
}

@Composable
fun BottomBar(
    modifier: Modifier,
    onNavigate: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SegmentedNavigationButton() {
            onNavigate(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedNavigationButton(
    onNavigate: (Int) -> Unit
) {
    val options = remember { mutableStateListOf("Home", "Data", "Juridisk") }
    var selectedIndex by remember { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                modifier = Modifier
                    .padding(bottom = 15.dp),
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onNavigate(index)
                },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {},
                colors = SegmentedButtonColors(
                    activeContainerColor = Color(0xFF9DDDF9),
                    activeBorderColor = Color.DarkGray,
                    activeContentColor = Color.Black,
                    inactiveBorderColor = Color.DarkGray,
                    inactiveContainerColor = Color.Unspecified,
                    inactiveContentColor = Color.Black,
                    disabledActiveBorderColor = Color.DarkGray,
                    disabledActiveContainerColor = Color.Unspecified,
                    disabledActiveContentColor = Color.Black,
                    disabledInactiveBorderColor = Color.DarkGray,
                    disabledInactiveContainerColor = Color.Unspecified,
                    disabledInactiveContentColor = Color.Black
                ),
            )
            {
                Text(text = option)
            }
        }
    }
}