package com.example.pressureapp

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeDoctorScreen(onClickLogout: () -> Unit = {}, onPacienteClick: (String) -> Unit)
{
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

    Column(modifier = Modifier.fillMaxSize()){
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
                .background(MaterialTheme.colorScheme.onBackground)
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
                        tint = colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if (user != null) {
                        Text(doctorName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = colorScheme.onPrimary)
                    } else {
                        Text("No hay usuario",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = colorScheme.onPrimary)
                    }
                }

                Button(
                    onClick = {
                        auth.signOut()
                        onClickLogout()
                    },
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.secondary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Pacientes",
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary)

            Spacer(modifier = Modifier.height(12.dp))

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
                            colors = CardDefaults.cardColors(Color.LightGray),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically)
                                {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Ícono user",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = nombre,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold)
                                }
                                Text(text = email, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}