package org.bmach01.ackey.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.bmach01.ackey.ui.AppScreen
import org.bmach01.ackey.ui.viewmodel.AcKeyAppViewModel

@Composable
fun AcKeyApp(
    navController: NavHostController = rememberNavController(),
    viewmodel: AcKeyAppViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = viewmodel.getInitialScreen().name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = AppScreen.RegisterScreen.name) {
            MainRegisterView (
                navigateToLoginSetup = {
                    navController.navigate(AppScreen.LoginSetupScreen.name)
                }
            )
        }
        composable(route = AppScreen.LoginSetupScreen.name) {
            MainLoginSetupView (
                navigateToKey = {
                    navController.navigate(AppScreen.KeyScreen.name)
                }
            )
        }
        composable(route = AppScreen.LoginScreen.name) {
            MainLoginScreenView(
                navigateToKey = {
                        navController.navigate(AppScreen.KeyScreen.name)
                    },
                navigateToRegistration = { navController.navigate(AppScreen.RegisterScreen.name) }
            )
        }
        composable(route = AppScreen.KeyScreen.name) {
            MainKeyView(
                navigateToSettings = { navController.navigate(AppScreen.SettingsScreen.name) }
            )
        }
        composable(route = AppScreen.SettingsScreen.name) {
            MainSettingsView (
                goBack = {
                    if (!navController.popBackStack(AppScreen.KeyScreen.name, true)) {
                        navController.navigate(AppScreen.KeyScreen.name)
                    }
                },
                navigateToRegistration = {
                    navController.navigate(AppScreen.RegisterScreen.name)
                },
                navigateToChangePIN = {
                    navController.navigate(AppScreen.ChangePINScreen.name)
                }
            )
        }
        composable(route = AppScreen.ChangePINScreen.name) {
            MainChangePINView(
                goBack = {
                    if (!navController.popBackStack(AppScreen.SettingsScreen.name, true)) {
                        navController.navigate(AppScreen.SettingsScreen.name)
                    }
                }
            )
        }
    }
}