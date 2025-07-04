package com.example.pressureapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DOCTOR_HOME = "doctor_home"
    const val PACIENTE_HOME = "paciente_home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { _, role ->
                    when (role) {
                        "doctor" -> navController.navigate(Routes.DOCTOR_HOME)
                        "paciente" -> navController.navigate(Routes.PACIENTE_HOME)
                        else -> navController.navigate(Routes.PACIENTE_HOME)
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DOCTOR_HOME) {
            HomeDoctorScreen()
        }

        composable(Routes.PACIENTE_HOME) {
            HomePacienteScreen()
        }
    }
}