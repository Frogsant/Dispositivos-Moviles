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
    const val PACIENTE_LISPRES = "paciente_lispres"
    const val PACIENTE_EXTRA_DATA = "paciente_extra_data"
    const val REGISTRO_INDIVIDUAL = "registro_individual"
    const val DOCTOR_LISTA_PRESIONES = "doctor_lista_presiones"
    const val DOCTOR_REGISTRO_INDIVIDUAL = "doctor_registro_individual"
    const val EDITAR_NOTA_MEDICA = "editar_nota_medica"

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
            HomeDoctorScreen(
                onClickLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                    }
                },
                onPacienteClick = { pacienteId ->
                    navController.navigate("${Routes.DOCTOR_LISTA_PRESIONES}/$pacienteId")
                }
            )
        }


        composable(Routes.PACIENTE_HOME) {
            HomePacienteScreen(
                onClickLogout = {
                    navController.navigate(Routes.LOGIN){
                        popUpTo(0)
                    }
                },
                onNavigateToListaPresiones = {
                    navController.navigate(Routes.PACIENTE_LISPRES)
                }
            )
        }

        composable(Routes.PACIENTE_LISPRES) {
            ListaPresionesPacienteScreen(
                onRegistroClick = { registroId ->
                    navController.navigate("${Routes.REGISTRO_INDIVIDUAL}/$registroId")
                }
            )
        }

        composable("${Routes.REGISTRO_INDIVIDUAL}/{registroId}") { backStackEntry ->
            val registroId = backStackEntry.arguments?.getString("registroId") ?: ""
            RegistroIndividualScreen(registroId = registroId)
        }


        composable("${Routes.DOCTOR_REGISTRO_INDIVIDUAL}/{pacienteId}/{registroId}") { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: ""
            val registroId = backStackEntry.arguments?.getString("registroId") ?: ""
            RegistroIndividualDoctorScreen(
                pacienteId = pacienteId,
                registroId = registroId,
                navController = navController
            )
        }

        composable("${Routes.DOCTOR_LISTA_PRESIONES}/{pacienteId}") { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: ""
            ListaPresionesDoctorScreen(
                pacienteId = pacienteId,
                onRegistroClick = { registroId ->
                    navController.navigate("${Routes.DOCTOR_REGISTRO_INDIVIDUAL}/$pacienteId/$registroId")
                }
            )
        }

        composable("${Routes.EDITAR_NOTA_MEDICA}/{pacienteId}/{registroId}") { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: ""
            val registroId = backStackEntry.arguments?.getString("registroId") ?: ""
            MedicalNoteEdit(pacienteId = pacienteId, registroId = registroId, navController = navController)
        }
    }
}
