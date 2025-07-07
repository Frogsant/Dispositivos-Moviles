package com.example.pressureapp

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun RegisterScreen(navController: NavController, onNavigateBack: () -> Unit)
{
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("")}
    var usernameError by remember { mutableStateOf("")}
    var passwordError by remember { mutableStateOf("")}
    var confirmPasswordError by remember { mutableStateOf("")}
    var roleError by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar usuario", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, autoCorrect = false),
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
            supportingText = {
                if (emailError.isNotEmpty()){
                    Text(text = emailError, color = Color.Red)
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Nombre de usuario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
            supportingText = {
                if (usernameError.isNotEmpty()){
                    Text(text = usernameError, color = Color.Red)
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
            supportingText = {
                if (passwordError.isNotEmpty()){
                    Text(text = passwordError, color = Color.Red)
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Confirmar contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false),
            visualTransformation = PasswordVisualTransformation(),
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
            supportingText = {
                if (confirmPasswordError.isNotEmpty()){
                    Text(text = confirmPasswordError, color = Color.Red)
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        RoleDropdown(
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it },
            supportingText = {
                if (roleError.isNotEmpty()){
                    Text(text = roleError, color = Color.Red)
                }
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val isValidUsername = validateUsername(username).first
                val isValidEmail = validateEmail(email).first
                val isValidPassword = validatePassword(password).first
                val isValidConfirmPassword = validateConfirmPassword(password, confirmPassword).first
                val isValidRole = validateSelectedRole(selectedRole).first

                emailError = validateEmail(email).second
                usernameError = validateUsername(username).second
                passwordError = validatePassword(password).second
                confirmPasswordError = validateConfirmPassword(password, confirmPassword).second
                roleError = validateSelectedRole(selectedRole).second

                if (isValidUsername && isValidEmail && isValidPassword && isValidConfirmPassword && isValidRole) {
                    errorMessage = ""
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { authResult ->
                            val user = authResult.user!!
                            val roleCollection = when (selectedRole) {
                                "Paciente" -> "pacientes"
                                "Doctor" -> "doctores"
                                else -> "otros"
                            }
                            Firebase.firestore.collection(roleCollection)
                                .document(user.uid)
                                .set(
                                    mapOf(
                                        "email" to email,
                                        "username" to username,
                                        "role" to selectedRole
                                    )
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Registrado como $selectedRole", Toast.LENGTH_SHORT).show()

                                    if (selectedRole == "Paciente") {
                                        navController.navigate("${Routes.PACIENTE_EXTRA_DATA}/${user.uid}")
                                    } else {
                                        navController.navigate(Routes.DOCTOR_HOME) {
                                            popUpTo(Routes.REGISTER) { inclusive = true }
                                        }
                                    }
                                }
                        }
                        .addOnFailureListener { error ->
                            errorMessage = when (error) {
                                is FirebaseAuthInvalidCredentialsException -> "Correo inválido"
                                is FirebaseAuthUserCollisionException -> "Correo ya registrado"
                                else -> "Error: ${error.message}"
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Registrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { onNavigateBack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleDropdown(selectedRole: String, onRoleSelected: (String) -> Unit, supportingText: @Composable (() -> Unit)? = null) {
    val roles = listOf("Paciente", "Doctor")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            value = selectedRole,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de usuario") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
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
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            supportingText = supportingText
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            roles.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role) },
                    onClick = {
                        onRoleSelected(role)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}