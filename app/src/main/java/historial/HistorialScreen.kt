package com.example.loginsigo.ui.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loginsigo.Routes // IMPORTANTE PARA LA NAVEGACIÓN
import com.example.loginsigo.data.model.UserResponse

// Definimos los colores aquí para que sean accesibles por DetalleCuatrimestreScreen también
val MockupTeal = Color(0xFF26A69A)
val BackgroundColor = Color(0xFFF5F5F5)

@Composable
fun HistorialScreen(
    navController: NavController,
    user: UserResponse
) {
    // DATOS FALSOS - 4 CUATRIMESTRES
    val historialMockup = remember {
        listOf(
            MockCuatrimestre(id = 4, badge = "4to", periodo = "Enero - Abril 2026", activo = true, carrera = "Ing. en Desarrollo de Software", grupo = "4A Matutino", tutor = "Dra. Gricelda Rodríguez Robledo", progreso = "35%", desempeno = "En Curso"),
            MockCuatrimestre(id = 3, badge = "3er", periodo = "Septiembre - Diciembre 2025", activo = false, carrera = "TSU en Desarrollo de Software", grupo = "3B Matutino", tutor = "Ing. Juan Pérez", progreso = "100%", desempeno = "Competente"),
            MockCuatrimestre(id = 2, badge = "2do", periodo = "Mayo - Agosto 2025", activo = false, carrera = "TSU en Desarrollo de Software", grupo = "2B Matutino", tutor = "Lic. María González", progreso = "100%", desempeno = "Competente"),
            MockCuatrimestre(id = 1, badge = "1er", periodo = "Enero - Abril 2025", activo = false, carrera = "TSU en Desarrollo de Software", grupo = "1A Matutino", tutor = "Ing. Pedro López", progreso = "100%", desempeno = "Competente")
        )
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(60.dp).background(color = MockupTeal, shape = RoundedCornerShape(12.dp))
            ) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                    Text(text = "Historial Académico", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                }
            }
        },
        containerColor = BackgroundColor
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.height(50.dp).fillMaxWidth().background(MockupTeal, RoundedCornerShape(25.dp)))
                    Text(text = user.username, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(historialMockup) { item ->
                // Pasamos el navController a la tarjeta
                CuatrimestreCard(item, navController)
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun CuatrimestreCard(item: MockCuatrimestre, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().background(MockupTeal).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                        Text(text = item.badge, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Cuatrimestre", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = item.periodo, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    }

                    // --- BOTÓN "+" AHORA FUNCIONA ---
                    IconButton(
                        onClick = {
                            // Navega a la nueva pantalla pasando el ID del cuatrimestre
                            navController.navigate(Routes.detalleCuatrimestre(item.id))
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Ver detalles", tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    // --------------------------------
                }
            }
            Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9)).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "●", color = if (item.activo) MockupTeal else Color.Gray, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if(item.activo) "Estatus: Activo" else "Estatus: Inactivo", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                }
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                DetailRow("Carrera", item.carrera)
                DetailRow("Grupo", item.grupo)
                DetailRow("Tutor", item.tutor)
                DetailRow("Progreso", item.progreso)
                DetailRow("Desempeño", item.desempeno)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.Gray, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Text(text = value, color = Color.DarkGray, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(start = 16.dp))
    }
}

data class MockCuatrimestre(val id: Int, val badge: String, val periodo: String, val activo: Boolean, val carrera: String, val grupo: String, val tutor: String, val progreso: String, val desempeno: String)