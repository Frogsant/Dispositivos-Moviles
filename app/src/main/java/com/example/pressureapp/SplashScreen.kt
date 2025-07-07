package com.example.pressureapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun SplashScreen(onRoleDetected: (String) -> Unit) {
    LaunchedEffect(Unit) {
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val firestore = Firebase.firestore

            firestore.collection("pacientes").document(userId).get()
                .addOnSuccessListener { pacienteDoc ->
                    if (pacienteDoc.exists()) {
                        onRoleDetected("Paciente")
                    } else {
                        firestore.collection("doctores").document(userId).get()
                            .addOnSuccessListener { doctorDoc ->
                                if (doctorDoc.exists()) {
                                    onRoleDetected("Doctor")
                                } else {
                                    onRoleDetected("")
                                }
                            }
                            .addOnFailureListener {
                                onRoleDetected("")
                            }
                    }
                }
                .addOnFailureListener {
                    onRoleDetected("")
                }
        } else {
            onRoleDetected("")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}