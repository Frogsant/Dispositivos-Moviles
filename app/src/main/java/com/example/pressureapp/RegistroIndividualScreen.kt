package com.example.pressureapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun RegistroIndividualScreen(registroId: String, navController: NavController) {
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

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    )
    {
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
                    text = "Detalle del Registro",
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (data != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(16.dp))
                {
                    RegistroItem(
                        label = "Fecha",
                        value = data?.get("fecha") as? String ?: "-",
                        icon = Icons.Default.CalendarMonth
                    )

                    RegistroItem(
                        label = "Sistólica",
                        value = "${data?.get("sistolica")} mmHg",
                        icon = Icons.Default.Favorite
                    )

                    RegistroItem(
                        label = "Diastólica",
                        value = "${data?.get("diastolica")} mmHg",
                        icon = Icons.Default.FavoriteBorder
                    )

                    RegistroItem(
                        label = "Comentario del paciente",
                        value = data?.get("comentarioPaciente") as? String ?: "-",
                        icon = Icons.Default.Comment
                    )

                    RegistroItem(
                        label = "Nota médica",
                        value = data?.get("notaMedicaDoctor") as? String ?: "-",
                        icon = Icons.Default.NoteAlt
                    )

                    RegistroItem(
                        label = "Fecha nota médica",
                        value = data?.get("fechaNotaMedica") as? String ?: "-",
                        icon = Icons.Default.CalendarMonth
                    )

                    RegistroItem(
                        label = "Doctor que editó",
                        value = data?.get("doctorQueEditoNota") as? String ?: "-",
                        icon = Icons.Default.Person4
                    )
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Volver")
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun RegistroItem(label: String, value: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}