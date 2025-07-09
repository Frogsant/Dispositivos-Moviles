package com.example.pressureapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun HomePacienteScreen(onClickLogout: () -> Unit = {}, onNavigateToListaPresiones: () -> Unit = {}) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val user = auth.currentUser

    var username by remember { mutableStateOf("") }
    var sisto by remember { mutableStateOf("") }
    var diasto by remember { mutableStateOf("") }
    var comen by remember { mutableStateOf("") }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    var selectedDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        selectedDate = dateFormat.format(Date())
    }

    LaunchedEffect(user) {
        user?.let {
            Firebase.firestore.collection("pacientes").document(it.uid).get()
                .addOnSuccessListener { document ->
                    username = document.getString("username") ?: "Sin nombre"
                }
                .addOnFailureListener {
                    username = "Error al obtener nombre"
                }
        }
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                .background(Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp,
                        end = 16.dp,
                        top = 36.dp,
                        bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Ícono de usuario",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if (user != null) {
                        Text(username,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text("No hay usuario",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary)
                    }
                }

                Button(
                    onClick = {
                        auth.signOut()
                        onClickLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text("Registra tu presión",
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                    .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ){
                Text("Presión sistólica (máxima):")
                TextField(
                    value = sisto,
                    onValueChange = { sisto = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej. 120") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Presión diastólica (mínima):")
                TextField(
                    value = diasto,
                    onValueChange = { diasto = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej. 80") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Comentario:")
                TextField(
                    value = comen,
                    onValueChange = { comen = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Agregar comentario") },
                    keyboardOptions = KeyboardOptions.Default,
                    maxLines = 3,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Fecha de captura: $selectedDate",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val uid = user?.uid
                    if (uid != null && sisto.isNotBlank() && diasto.isNotBlank()) {
                        val firestore = Firebase.firestore
                        val nuevaPresion = hashMapOf(
                            "fecha" to selectedDate,
                            "sistolica" to sisto,
                            "diastolica" to diasto,
                            "comentarioPaciente" to comen,
                            "notaMedicaDoctor" to "",
                            "fechaNotaMedica" to "",
                            "doctorQueEditoNota" to ""
                        )

                        firestore.collection("pacientes")
                            .document(uid)
                            .collection("presiones")
                            .add(nuevaPresion)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Presión registrada", Toast.LENGTH_SHORT).show()
                                sisto = ""
                                diasto = ""
                                comen = ""
                                selectedDate = dateFormat.format(Date())
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onNavigateToListaPresiones,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver mis registros")
            }
        }
    }
}