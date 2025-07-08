package com.example.pressureapp

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.navigation.NavController


@Composable
fun RegistroIndividualDoctorScreen(
    pacienteId: String,
    registroId: String,
    navController: NavController
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf<Map<String, Any>?>(null) }

    LaunchedEffect(registroId) {
        Firebase.firestore.collection("pacientes")
            .document(pacienteId)
            .collection("presiones")
            .document(registroId)
            .get()
            .addOnSuccessListener { document ->
                data = document.data
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al obtener registro", Toast.LENGTH_SHORT).show()
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Detalle del Registro", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            if (data != null) {
                InfoItem("📅 Fecha", data?.get("fecha") as? String ?: "-")
                InfoItem("🩺 Sistólica", "${data?.get("sistolica")} mmHg")
                InfoItem("💓 Diastólica", "${data?.get("diastolica")} mmHg")
                InfoItem("📝 Comentario del paciente", data?.get("comentarioPaciente") as? String ?: "-")
                InfoItem("🧑‍⚕️ Nota médica", data?.get("notaMedicaDoctor") as? String ?: "-")
                InfoItem("📆 Fecha nota médica", data?.get("fechaNotaMedica") as? String ?: "-")
                InfoItem("👨‍⚕️ Doctor que editó", data?.get("doctorQueEditoNota") as? String ?: "-")

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate("${Routes.EDITAR_NOTA_MEDICA}/$pacienteId/$registroId")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Editar nota médica")
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun InfoItemDoctor(label: String, value: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}