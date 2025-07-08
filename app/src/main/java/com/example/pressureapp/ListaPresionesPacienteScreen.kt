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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable


@Composable
fun ListaPresionesPacienteScreen(
    onRegistroClick: (String) -> Unit
) {
    val auth = Firebase.auth
    val user = Firebase.auth.currentUser
    val registros = remember { mutableStateListOf<Pair<String, Map<String, Any>>>() }

    LaunchedEffect(user) {
        user?.let {
            Firebase.firestore.collection("pacientes")
                .document(it.uid)
                .collection("presiones")
                .get()
                .addOnSuccessListener { result ->
                    registros.clear()
                    for (doc in result) {
                        registros.add(doc.id to doc.data)
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Registros de Presión",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (registros.isEmpty()) {
            Spacer(Modifier.height(32.dp))
            Text("No hay registros aún.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(registros) { (id, data) ->
                    val sistolica = data["sistolica"] as? String ?: "-"
                    val diastolica = data["diastolica"] as? String ?: "-"
                    val fecha = data["fecha"] as? String ?: "-"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .padding(vertical = 6.dp)
                            .clickable { onRegistroClick(id) },
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📅 $fecha", style = MaterialTheme.typography.bodyLarge)
                            Text("🩺 Sistólica: $sistolica mmHg", style = MaterialTheme.typography.bodyMedium)
                            Text("💓 Diastólica: $diastolica mmHg", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
