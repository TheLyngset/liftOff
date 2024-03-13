package no.uio.ifi.in2000.team_17.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun App(){
    val uiViewModel: UiViewModel = viewModel()
    val uiState by uiViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = Screen.Home,
                canNavigateBack = false,
                navigateUp = {}
            )
        }
    ){innerPadding ->
        HomeScreen(Modifier.padding(innerPadding),uiState)
    }
}