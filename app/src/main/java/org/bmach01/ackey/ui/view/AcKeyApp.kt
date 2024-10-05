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
    initialScreen: AppScreen = AppScreen.RegisterScreen
) {
    NavHost(
        navController = navController,
        startDestination = initialScreen.name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = AppScreen.KeyScreen.name) {
            MainKeyView {
                navController.navigate(it)
            }
        }
        composable(route = AppScreen.PasswordKeyboardScreen.name) {
            MainPasswordKeyboardView {
                navController.navigate(it)
            }
        }
        composable(route = AppScreen.RegisterScreen.name) {
            MainRegisterView {
                navController.navigate(it)
            }
        }
        composable(route = AppScreen.SettingsScreen.name) {
            MainSettingsView {
                navController.navigate(it)
            }
        }
    }
}