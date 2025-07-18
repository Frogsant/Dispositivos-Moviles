package com.example.pressureapp

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ListaPresionesDoctorScreen(pacienteId: String, onRegistroClick: (String) -> Unit, navController: NavController)
{
    val context = LocalContext.current
    val registros = remember { mutableStateListOf<Pair<String, Map<String, Any>>>() }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    LaunchedEffect(pacienteId) {
        Firebase.firestore.collection("pacientes")
            .document(pacienteId)
            .collection("presiones")
            .get()
            .addOnSuccessListener { result ->
                val rawList = result.map { it.id to it.data }
                val sortedList = rawList.sortedByDescending { pair ->
                    val dateString = pair.second["fecha"] as? String ?: ""
                    try {
                        dateFormat.parse(dateString)
                    } catch (e: Exception) {
                        null
                    }
                }
                registros.clear()
                registros.addAll(sortedList)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al obtener registros", Toast.LENGTH_SHORT).show()
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 42.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Registros de Presión",
                        fontSize = 28.sp,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (registros.isEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        "No hay registros disponibles.", style = MaterialTheme.typography.bodyLarge)
                } else {
                    registros.forEach { (id, data) ->
                        val sistolica = data["sistolica"] as? String ?: "-"
                        val diastolica = data["diastolica"] as? String ?: "-"
                        val fecha = data["fecha"] as? String ?: "-"

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(vertical = 6.dp)
                                .clickable { onRegistroClick(id) },
                            colors = CardDefaults.cardColors(Color.LightGray),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start)
                                {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Ícono calendario",
                                        tint = MaterialTheme.colorScheme.surface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(fecha, style = MaterialTheme.typography.bodyLarge)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically)
                                {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Ícono médico",
                                        tint = MaterialTheme.colorScheme.surface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sistólica: $sistolica mmHg",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Ícono corazón",
                                        tint = MaterialTheme.colorScheme.surface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Diastólica: $diastolica mmHg",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Volver")
        }
    }
}