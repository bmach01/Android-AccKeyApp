package org.bmach01.ackey.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.bmach01.ackey.ui.AppScreen

@Composable
fun AcKeyApp(
    navController: NavHostController = rememberNavController(),
    initialScreen: AppScreen = AppScreen.KeyScreen
) {
    NavHost(
        navController = navController,
        startDestination = initialScreen.name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = AppScreen.RegisterScreen.name) {
            MainRegisterView (
                navigateTo = navController::navigate
            )
        }
        composable(route = AppScreen.PINKeyboardScreen.name) {
            MainLoginView (
                navigateTo = navController::navigate
            )
        }
        composable(route = AppScreen.KeyScreen.name) {
            MainKeyView(
                navigateTo = navController::navigate
            )
        }
        composable(route = AppScreen.SettingsScreen.name) {
            MainSettingsView (
                goBack = navController::popBackStack
            )
        }
    }
}