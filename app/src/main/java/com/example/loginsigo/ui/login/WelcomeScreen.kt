package com.example.loginsigo.ui.login

import android.net.Uri // Importante para evitar errores en la navegación
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loginsigo.data.model.UserResponse
import com.google.gson.Gson // Necesitamos GSON para empaquetar al usuario

// --- TUS COLORES OFICIALES ---

val SigoFondoCard = Color(0xFFF5F2F9)
val SigoTexto = Color(0xFF2C2C2C)
val SigoGris = Color(0xFF5A5A5A)

@Composable
fun WelcomeScreen(
    user: UserResponse,
    navController: NavController
) {
    // Variable para la alerta de cerrar sesión
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Barra Superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
                    .background(color = SigoTurquesa, shape = RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                    Text(
                        text = "Mi Perfil",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White)
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // --- 1. CABECERA CON AVATAR ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(SigoFondoCard),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = SigoTurquesa
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.personFullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SigoTexto,
                    fontSize = 20.sp
                )
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SigoGris
                )
            }

            // --- 2. TARJETA: INFORMACIÓN PERSONAL ---
            InfoCard(title = "Información Personal", icon = Icons.Default.Face) {
                InfoRow("Email", user.email)
                InfoRow("Perfil", user.profileName)
                InfoRow("ID Persona", user.personId.toString())
            }

            // --- 3. TARJETA: DETALLES DE CUENTA ---
            InfoCard(title = "Detalles de Cuenta", icon = Icons.Default.Settings) {
                InfoRow("Módulo", user.accessModule)
                InfoRow(
                    "Estado",
                    if (user.active) "Activo" else "Inactivo",
                    colorValor = if(user.active) SigoTurquesa else Color.Red
                )
                InfoRow("Registro", user.register)
                InfoRow("Términos", if (user.termsConditions) "Aceptados" else "Pendiente")

                if (user.roles.isNotEmpty()) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = SigoTurquesa.copy(alpha = 0.2f))
                    Text("Roles Asignados:", fontSize = 12.sp, color = SigoGris)
                    user.roles.forEach { role ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top=4.dp)) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp), tint = SigoTurquesa)
                            Text(" $role", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = SigoTexto)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. BOTÓN HISTORIAL (AQUÍ ESTABA EL ERROR) ---
            Button(
                onClick = {
                    // *** SOLUCIÓN DEL CRASH ***
                    // 1. Convertimos el usuario a JSON (Texto)
                    val userJson = Gson().toJson(user)
                    // 2. Codificamos el texto para que sea seguro viajar en la URL (evita errores con llaves {})
                    val userJsonSeguro = Uri.encode(userJson)

                    // 3. Navegamos pasando el paquete completo
                    navController.navigate("historial_screen/$userJsonSeguro")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SigoTurquesa),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Ver Historial Académico",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ID Registro
            Text(
                text = "ID Registro: ${user.id}",
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 30.dp),
                fontSize = 12.sp,
                color = Color.LightGray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 5. BOTÓN CERRAR SESIÓN ---
            Button(
                onClick = { showLogoutDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Alerta de Logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Cerrar Sesión") },
            text = { Text(text = "¿Estás seguro de que quieres salir de la aplicación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        navController.navigate("login_screen") { popUpTo(0) }
                    }
                ) {
                    Text("Sí, salir", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            containerColor = Color.White
        )
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun InfoCard(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SigoFondoCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                Icon(imageVector = icon, contentDescription = null, tint = SigoTurquesa, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontWeight = FontWeight.Bold, color = SigoTexto, fontSize = 16.sp)
            }
            Divider(color = SigoTurquesa.copy(alpha = 0.3f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, valor: String, colorValor: Color = SigoTexto) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = SigoGris, fontWeight = FontWeight.Normal)
        Text(text = valor, fontSize = 14.sp, color = colorValor, fontWeight = FontWeight.SemiBold)
    }
}