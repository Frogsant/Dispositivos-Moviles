package com.example.pressureapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DOCTOR_HOME = "doctor_home"
    const val PACIENTE_HOME = "paciente_home"
    const val PACIENTE_EXTRA_DATA = "paciente_extra_data"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onRoleDetected = { role ->
                when (role) {
                    "Doctor" -> navController.navigate(Routes.DOCTOR_HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                    "Paciente" -> navController.navigate(Routes.PACIENTE_HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                    else -> navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            })
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { _, role ->
                    when (role) {
                        "Doctor" -> navController.navigate(Routes.DOCTOR_HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }

                        "Paciente" -> navController.navigate(Routes.PACIENTE_HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("${Routes.PACIENTE_EXTRA_DATA}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            PacienteExtraDataScreen(userId = userId) {
                navController.navigate(Routes.PACIENTE_HOME) {
                    popUpTo(Routes.REGISTER) { inclusive = true }
                }
            }
        }

        composable(Routes.DOCTOR_HOME) {
            HomeDoctorScreen(onClickLogout = {
                navController.navigate(Routes.LOGIN){
                    popUpTo(0)
                }
            })
        }

        composable(Routes.PACIENTE_HOME) {
            HomePacienteScreen(onClickLogout = {
                navController.navigate(Routes.LOGIN){
                    popUpTo(0)
                }
            })
        }
    }
}


