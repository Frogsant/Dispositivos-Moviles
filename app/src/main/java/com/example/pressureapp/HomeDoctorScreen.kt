package com.example.pressureapp

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items

@Composable
fun HomeDoctorScreen(onClickLogout: () -> Unit = {}, onPacienteClick: (String) -> Unit) {
    val auth = Firebase.auth
    val user = auth.currentUser
    val context = LocalContext.current

    var doctorName by remember { mutableStateOf("") }
    val pacientes = remember { mutableStateListOf<Pair<String, Map<String, Any>>>() }

    LaunchedEffect(user) {
        user?.let {
            Firebase.firestore.collection("doctores").document(it.uid).get()
                .addOnSuccessListener { document ->
                    doctorName = document.getString("username") ?: "Sin nombre"
                }
                .addOnFailureListener {
                    doctorName = "Error al obtener nombre"
                }

            Firebase.firestore.collection("pacientes")
                .get()
                .addOnSuccessListener { result ->
                    pacientes.clear()
                    for (doc in result) {
                        pacientes.add(doc.id to doc.data)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener pacientes", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido, Doctor", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
        Text(doctorName, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Pacientes registrados", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (pacientes.isEmpty()) {
            Text("No hay pacientes registrados.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pacientes) { (id, data) ->
                    val nombre = data["username"] as? String ?: "Sin nombre"
                    val email = data["email"] as? String ?: "Sin correo"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPacienteClick(id) },
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = nombre, style = MaterialTheme.typography.bodyLarge)
                            Text(text = email, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                auth.signOut()
                onClickLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}