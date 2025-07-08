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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable


@Composable
fun ListaPresionesDoctorScreen(pacienteId: String, onRegistroClick: (String) -> Unit) {
    val context = LocalContext.current
    val registros = remember { mutableStateListOf<Pair<String, Map<String, Any>>>() }

    LaunchedEffect(pacienteId) {
        Firebase.firestore.collection("pacientes")
            .document(pacienteId)
            .collection("presiones")
            .get()
            .addOnSuccessListener { result ->
                registros.clear()
                for (doc in result) {
                    registros.add(doc.id to doc.data)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al obtener registros", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Presiones del Paciente", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        if (registros.isEmpty()) {
            Text("No hay registros disponibles.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(registros) { (id, data) ->
                    val sistolica = data["sistolica"] as? String ?: "-"
                    val diastolica = data["diastolica"] as? String ?: "-"
                    val fecha = data["fecha"] as? String ?: "-"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRegistroClick(id) },
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("📅 Fecha: $fecha", style = MaterialTheme.typography.bodyLarge)
                            Text("🩺 Sistólica: $sistolica mmHg", style = MaterialTheme.typography.bodyMedium)
                            Text("💓 Diastólica: $diastolica mmHg", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}