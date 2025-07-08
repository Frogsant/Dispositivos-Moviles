package com.example.pressureapp

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun RegistroIndividualScreen(registroId: String) {
    val auth = Firebase.auth
    val user = Firebase.auth.currentUser
    var data by remember { mutableStateOf<Map<String, Any>?>(null) }

    LaunchedEffect(registroId) {
        user?.let {
            Firebase.firestore.collection("pacientes")
                .document(it.uid)
                .collection("presiones")
                .document(registroId)
                .get()
                .addOnSuccessListener { document ->
                    data = document.data
                }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detalle del Registro",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (data != null) {
                InfoItem("📅 Fecha", data?.get("fecha") as? String ?: "-")
                InfoItem("🩺 Sistólica", "${data?.get("sistolica")} mmHg")
                InfoItem("💓 Diastólica", "${data?.get("diastolica")} mmHg")
                InfoItem("📝 Comentario del paciente", data?.get("comentarioPaciente") as? String ?: "-")
                InfoItem("🧑‍⚕️ Nota médica", data?.get("notaMedicaDoctor") as? String ?: "-")
                InfoItem("📆 Fecha nota médica", data?.get("fechaNotaMedica") as? String ?: "-")
                InfoItem("👨‍⚕️ Doctor que editó", data?.get("doctorQueEditoNota") as? String ?: "-")
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
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