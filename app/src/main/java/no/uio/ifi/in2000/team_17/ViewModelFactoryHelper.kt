package no.uio.ifi.in2000.team_17

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
/**
 * Helper function to create a ViewModelProvider.Factory from a lambda
 */
fun <VM: ViewModel> viewModelFactory(initializer:()-> VM):ViewModelProvider.Factory{
    return object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return initializer() as T
        }
    }
}