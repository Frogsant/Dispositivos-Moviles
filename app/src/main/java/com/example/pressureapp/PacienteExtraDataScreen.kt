package com.example.pressureapp

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun PacienteExtraDataScreen(userId: String, onRegisterComplete: () -> Unit)
{
    val context = LocalContext.current

    var edad by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = edad,
            onValueChange = { edad = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Edad") },
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
            ),
        )

        Spacer(modifier = Modifier.height(6.dp))

        TextField(
            value = altura,
            onValueChange = { altura = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Altura (cm)") },
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
            ),
        )

        Spacer(modifier = Modifier.height(6.dp))

        TextField(
            value = peso,
            onValueChange = { peso = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Peso (kg)") },
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
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val firestore = Firebase.firestore

            firestore.collection("pacientes").document(userId)
                .update(
                    mapOf(
                        "edad" to edad,
                        "altura" to altura,
                        "peso" to peso
                    )
                ).addOnSuccessListener {
                    Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                    onRegisterComplete()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error al guardar: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Finalizar registro")
        }
    }
}