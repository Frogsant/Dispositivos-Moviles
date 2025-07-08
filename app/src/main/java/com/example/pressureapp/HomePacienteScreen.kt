package com.example.pressureapp

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomePacienteScreen(onClickLogout: () -> Unit = {},
                       onNavigateToListaPresiones: () -> Unit = {}
) {
    val context = LocalContext.current

    val auth = Firebase.auth
    val user = auth.currentUser
    var username by remember { mutableStateOf("") }

    var sisto by remember { mutableStateOf("") }
    var diasto by remember { mutableStateOf("") }
    var comen by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Top-right logout button
        Button(
            onClick = {
                auth.signOut()
                onClickLogout()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp), // Add padding from the edges
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Cerrar Sesión")
        }

        // Main content in the center
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bienvenido, Paciente", fontSize = 24.sp)
            if (user != null) {
                Text(username)
            } else {
                Text("No hay usuario")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Registra tus presiones y comentario:", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(6.dp))

            Text("Presión sistólica (máxima):", fontSize = 16.sp)
            TextField(
                value = sisto,
                onValueChange = { sisto = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Sistólica (máxima)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrect = false),
                singleLine = true,
                maxLines = 1,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Presión diastólica (máxima):", fontSize = 16.sp)
            TextField(
                value = diasto,
                onValueChange = { diasto = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Diastólica (mínima)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrect = false),
                singleLine = true,
                maxLines = 1,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Comentario:", fontSize = 16.sp)
            TextField(
                value = comen,
                onValueChange = { comen = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Agregar comentario.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified, autoCorrect = true),
                singleLine = true,
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
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Spacer(modifier = Modifier.height(6.dp))

            Text("Fecha de captura:", fontSize = 16.sp)
            TextField(
                value = selectedDate,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                placeholder = { Text("Seleccionar fecha") },
                readOnly = true,
                enabled = false, // prevents keyboard from appearing
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Button(
                onClick = {
                    val uid = user?.uid
                    if (uid != null && sisto.isNotBlank() && diasto.isNotBlank() && selectedDate.isNotBlank()) {
                        val firestore = Firebase.firestore

                        val nuevaPresion = hashMapOf(
                            "fecha" to selectedDate,
                            "sistolica" to sisto,
                            "diastolica" to diasto,
                            "comentarioPaciente" to comen,
                            "notaMedicaDoctor" to "",          // vacío por defecto
                            "fechaNotaMedica" to "",           // vacío por defecto
                            "doctorQueEditoNota" to ""         // vacío por defecto
                        )

                        firestore.collection("pacientes")
                            .document(uid)
                            .collection("presiones")
                            .add(nuevaPresion)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Presión registrada correctamente", Toast.LENGTH_SHORT).show()
                                // Limpia los campos
                                sisto = ""
                                diasto = ""
                                comen = ""
                                selectedDate = ""
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error al registrar la presión", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ){
                Text("Registrar presión.")
            }

            Button(
                onClick = onNavigateToListaPresiones,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ){
                Text("Ver mis registros.")
            }

        }
    }
}