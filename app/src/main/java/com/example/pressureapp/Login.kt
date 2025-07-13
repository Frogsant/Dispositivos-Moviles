package com.example.pressureapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(onLoginSuccess: (FirebaseUser, String) -> Unit, onNavigateToRegister: () -> Unit)
{
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("")}
    var passwordError by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, Color.LightGray, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ){
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, autoCorrect = false),
                singleLine = true,
                maxLines = 1,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                supportingText = {
                    if (emailError.isNotEmpty()){
                        Text(text = emailError, color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            )

            Spacer(modifier = Modifier.height(2.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                maxLines = 1,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                supportingText = {
                    if (passwordError.isNotEmpty()){
                        Text(text = passwordError, color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val emailValidation = validateEmail(email)
                val passwordValidation = validatePassword(password)

                val isValidEmail = emailValidation.first
                val isValidPassword = passwordValidation.first

                emailError = emailValidation.second
                passwordError = passwordValidation.second

                if (isValidEmail && isValidPassword) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            val user = authResult.user
                            if (user != null) {
                                val firestore = Firebase.firestore
                                val userId = user.uid

                                firestore.collection("pacientes").document(userId).get()
                                    .addOnSuccessListener { pacienteDoc ->
                                        if (pacienteDoc.exists()) {
                                            onLoginSuccess(user, "Paciente")
                                        } else {
                                            firestore.collection("doctores").document(userId).get()
                                                .addOnSuccessListener { doctorDoc ->
                                                    if (doctorDoc.exists()) {
                                                        onLoginSuccess(user, "Doctor")
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Error: Rol no encontrado",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                                .addOnFailureListener { firestoreError ->
                                                    Toast.makeText(
                                                        context,
                                                        "Error al obtener datos: ${firestoreError.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                    }
                                    .addOnFailureListener { firestoreError ->
                                        Toast.makeText(
                                            context,
                                            "Error al obtener el rol: ${firestoreError.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { authError ->
                            Toast.makeText(context, "Error de autenticación: ${authError.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tienes cuenta? Regístrate", color = MaterialTheme.colorScheme.primary)
        }
    }
}