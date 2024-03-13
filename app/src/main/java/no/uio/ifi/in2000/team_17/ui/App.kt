package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class Screen(val title: String) {
    Home(title = "Home Screen"),
    Input(title = "Input Screen")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {}
 ){
    TopAppBar(
        title = { Text(currentScreen.title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
fun App(
    navController:NavHostController = rememberNavController(),

){
    val uiViewModel: UiViewModel = viewModel()
    val uiState by uiViewModel.uiState.collectAsState()
    val scrollStateVertical = rememberScrollState()
    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = Screen.Home,
                canNavigateBack = false,
                navigateUp = {}
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
        }
    ){innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.name
        ){
            composable(route = Screen.Home.name){
                HomeScreen(Modifier.padding(innerPadding).verticalScroll(scrollStateVertical),uiState = uiState)
            }
            composable(route = Screen.Input.name){
                InputScreen(Modifier.padding(innerPadding))
            }
        }
    }
}