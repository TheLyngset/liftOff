package no.uio.ifi.in2000.team_17.ui

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team_17.App
import no.uio.ifi.in2000.team_17.R
import no.uio.ifi.in2000.team_17.ui.data_screen.DataScreen
import no.uio.ifi.in2000.team_17.ui.data_screen.DataScreenViewModel
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreen
import no.uio.ifi.in2000.team_17.ui.home_screen.HomeScreenViewModel
import no.uio.ifi.in2000.team_17.ui.input_sheet.InputSheet
import no.uio.ifi.in2000.team_17.ui.input_sheet.InputSheetViewModel
import no.uio.ifi.in2000.team_17.ui.judicial_screen.JudicialScreen
import no.uio.ifi.in2000.team_17.ui.thresholds.ThresholdsScreen
import no.uio.ifi.in2000.team_17.ui.thresholds.ThresholdsViewModel
import no.uio.ifi.in2000.team_17.viewModelFactory

/**
 * WARNING, do not change the order as the [BottomBar] depends on it!!
 */
enum class Screen(val title: String, val logo: Int) {
    Home(title = "Home Screen", logo = R.drawable.logo_endelig),
    Data(title = "Data Screen", logo = R.drawable.logo_endelig),
    Judicial(title = "Judicial Screen", logo = R.drawable.logo_endelig),
    Thresholds(title = "Thresholds", logo = R.drawable.logo_endelig),
    TechnicalDetailsScreen(title = "Technical Details", logo = R.drawable.logo_endelig),
    Empty("", 0)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    windowSizeClass: WindowSizeClass,
    logoId: Int,
    onSearchClick: () -> Unit,
    onLogoClick: () -> Unit,

    //modifier: Modifier
) {
    if (windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {

        TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(0.dp, 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = logoId),
                        contentDescription = "Home",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { onLogoClick() }
                    )
                    //center
                    /*
                    Text(
                        text = "Oslo",
                        style = TextStyle(fontSize = 22.sp),
                        modifier = Modifier.padding()

                    )
                */




                    Image(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Location and Thresholds",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(horizontal = 8.dp)
                            .clickable {
                                onSearchClick()
                            }
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent
                //containerColor = Color.White.copy(alpha = 0.65f)
                //MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.height(50.dp)
        )
    }
}

/**
 * This is the main composable responsible for navigating between the
 * screens and creating the viewModels. It consists of a Scaffold composable
 * with a top and bottom app bar and a InputSheet that can be reached from
 * every screen in the app.
 */
@Composable
fun App(
    context: Context,
    windowSizeClass: WindowSizeClass,
) {
    //Using viewModel Factories to take the repository created in Main activity as a parameter
    val homeScreenViewModel = viewModel<HomeScreenViewModel>(
        factory = viewModelFactory {
            HomeScreenViewModel(
                App.appModule.repository,
                App.appModule.settingsRepository,
                App.appModule.thresholdsRepository
            )
        }
    )
    val inputSheetViewModel = viewModel<InputSheetViewModel>(
        factory = viewModelFactory {
            InputSheetViewModel(
                App.appModule.repository,
                App.appModule.settingsRepository
            )
        }
    )
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

    //dataScreenViewModel.resetShowTutorial()

    val navController: NavHostController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var sheetState by remember { mutableStateOf(false) }
    val currentScreen = Screen.valueOf(navController.currentBackStackEntryAsState().value?.destination?.route ?: Screen.Home.name)


    BackGroundImage()

    Scaffold(
        topBar = {
            AppTopBar(
                windowSizeClass,
                logoId = Screen.Home.logo,
                onSearchClick = { sheetState = true },
                onLogoClick = {
                    navController.navigate("Home")
                },
                //Modifier.shadow(elevation = 15.dp, spotColor = Color.DarkGray, shape = RoundedCornerShape(1.dp))
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Transparent),
                windowSizeClass,
                currentScreen = currentScreen
            ) {
                when (it) {
                    0 -> navController.navigate(Screen.Home.name)

                    1 -> navController.navigate(Screen.Data.name)

                    2 -> navController.navigate(Screen.Judicial.name)
                }

            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Box(modifier = Modifier.padding(bottom = 80.dp),) {
                    Popup(
                        alignment = Alignment.TopCenter,
                        onDismissRequest = {
                            it.dismiss()
                        }
                    ) {
                        Snackbar(it)
                    }
                }
            }
        }
    ) { innerPadding ->
        InputSheet(
            viewModel = inputSheetViewModel,
            failedToUpdate = {
                coroutineScope.launch { snackBarHostState.showSnackbar("failed to update, do you have internet connection?") }
                inputSheetViewModel.rollback()
            },
            setMaxHeight = {
                try {
                    inputSheetViewModel.setMaxHeight(it.toInt())
                    coroutineScope.launch { snackBarHostState.showSnackbar("Set max height to $it") }
                } catch (e: NumberFormatException) {
                    coroutineScope.launch { snackBarHostState.showSnackbar("Invalid Max Height") }
                }
            },
            setLat = {
                try {
                    val lat = it.toDouble()
                    if (58 < lat && lat < 64.25) {
                        inputSheetViewModel.setLat(lat)
                        coroutineScope.launch { snackBarHostState.showSnackbar("Set latitude to $it") }
                    } else {
                        coroutineScope.launch { snackBarHostState.showSnackbar("Invalid Latitude, it must be between 58.0 and 64.24") }
                    }
                } catch (e: NumberFormatException) {
                    coroutineScope.launch { snackBarHostState.showSnackbar("Invalid Latitude") }
                }
            },
            setLng = {
                try {
                    val lng = it.toDouble()
                    if (4 < lng && lng < 12.5) {
                        inputSheetViewModel.setLng(lng)
                        coroutineScope.launch { snackBarHostState.showSnackbar("Set longitude to $it") }
                    } else {
                        coroutineScope.launch { snackBarHostState.showSnackbar("Invalid longitude, it must be between 4.0 and 12.5") }
                    }
                } catch (e: NumberFormatException) {
                    coroutineScope.launch { snackBarHostState.showSnackbar("Invalid Longitude") }
                }
            },
            toThresholdsScreen = {
                navController.navigate(Screen.Thresholds.name)
                sheetState = false
            },
            sheetState = sheetState,
            onDismiss = { sheetState = false },
        )

        NavHost(
            navController = navController,
            startDestination = Screen.Home.name
        ) {
            composable(
                enterTransition = {
                    when(initialState.destination.route){
                        Screen.Thresholds.name -> fadeIn(animationSpec = tween(300))
                        else -> slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Right
                        )
                    }
                },
                exitTransition = {
                    when(targetState.destination.route){
                        Screen.Thresholds.name -> fadeOut(animationSpec = tween(300))
                        else -> slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Left
                        )
                    }
                },
                route = Screen.Home.name
            ) {
                HomeScreen(Modifier.padding(innerPadding), homeScreenViewModel, windowSizeClass)
            }
            composable(
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(300)
                    )
                                 },
                route = Screen.Thresholds.name
            ) {
                ThresholdsScreen(
                    Modifier.padding(innerPadding),
                    windowHeightSizeClass = windowSizeClass.heightSizeClass,
                    viewModel = thresholdsViewModel,
                    onCorrectInputFormat = {
                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(
                                message = it
                            )
                        }
                    },
                ) {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = "Invalid input for ${it.titleId}"
                        )
                    }
                }
            }
            composable(
                enterTransition = {
                    when(initialState.destination.route){
                        Screen.Home.name -> slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Left
                        )
                        Screen.Thresholds.name -> fadeIn(animationSpec = tween(300))
                        else -> slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Right
                        )
                    }

                },
                exitTransition = {
                    when(targetState.destination.route){
                        Screen.Home.name -> slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Right
                        )
                        Screen.Thresholds.name -> fadeOut(animationSpec = tween(300))
                        else -> slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Left
                        )
                    }
                },
                route = Screen.Data.name
            ) {
                DataScreen(
                    context = context,
                    windowSizeClass = windowSizeClass,
                    modifier = Modifier.padding(innerPadding),
                    viewModel = dataScreenViewModel,
                ) { dataScreenViewModel.setTimeIndex(it) }
            }
            composable(
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    when(initialState.destination.route){
                        Screen.Thresholds.name -> fadeOut(animationSpec = tween(300))
                        else -> slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Right
                        )
                    }

                },
                route = Screen.Judicial.name
            ) {
                JudicialScreen(modifier = Modifier.padding(innerPadding), windowSizeClass)
            }
        }
    }

}

@Composable
fun BottomBar(
    modifier: Modifier,
    windowSizeClass: WindowSizeClass,
    currentScreen: Screen,
    onNavigate: (Int) -> Unit
) {
    if (windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SegmentedNavigationButton(
                currentScreen
            ) {
                onNavigate(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedNavigationButton(
    currentScreen: Screen,
    onNavigate: (Int) -> Unit
) {
    val options = remember { mutableStateListOf("Home", "Data", "Legal") }

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                selected = currentScreen.ordinal == index,
                onClick = {
                    onNavigate(index)
                },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {},
                colors = SegmentedButtonColors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeBorderColor = Color.DarkGray,
                    activeContentColor = Color.Black,
                    inactiveBorderColor = Color.DarkGray,
                    inactiveContainerColor = MaterialTheme.colorScheme.background.copy(1.0f),
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