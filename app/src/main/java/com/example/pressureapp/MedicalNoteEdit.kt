package com.example.pressureapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.NavController


@Composable
fun MedicalNoteEdit(
    pacienteId: String,
    registroId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val firestore = Firebase.firestore

    var nota by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user != null) {
            firestore.collection("doctores").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    doctorName = doc.getString("username") ?: "Sin nombre"
                }

            firestore.collection("pacientes")
                .document(pacienteId)
                .collection("presiones")
                .document(registroId)
                .get()
                .addOnSuccessListener { doc ->
                    nota = doc.getString("notaMedicaDoctor") ?: ""
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar nota médica",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, Color.LightGray, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ){
            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                label = { Text("Nota médica") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                firestore.collection("pacientes")
                    .document(pacienteId)
                    .collection("presiones")
                    .document(registroId)
                    .update(
                        mapOf(
                            "notaMedicaDoctor" to nota,
                            "fechaNotaMedica" to fechaActual,
                            "doctorQueEditoNota" to doctorName
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary
        )
        ) {
            Text("Volver")
        }
    }
}