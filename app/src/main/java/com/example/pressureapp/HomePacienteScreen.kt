package com.example.pressureapp

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun HomePacienteScreen(onClickLogout: () -> Unit = {}) {
    val auth = Firebase.auth
    val user = auth.currentUser
    var username by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            Firebase.firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    username = document.getString("username") ?: "Sin nombre"
                }
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column {
            Text("Bienvenido, Paciente", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

            if(user != null){
                Text(username)
            }else{
                Text("No hay usuario")
            }
            Button(
                onClick = {
                    auth.signOut()
                    onClickLogout()},
                colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary)
            ){

                Text("Cerrar Sesión")
            }
        }
    }
}