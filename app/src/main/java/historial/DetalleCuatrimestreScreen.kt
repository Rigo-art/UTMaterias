package com.example.loginsigo.ui.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info // Usamos Info que es seguro
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- CORRECCIÓN 1: Eliminamos las variables duplicadas ---
// MockupTeal y BackgroundColor ya las toma automáticamente de HistorialScreen.kt
// (Siempre y cuando ambos archivos estén en el mismo paquete 'ui.historial')

val TextGray = Color(0xFF666666)
val LabelGray = Color(0xFF888888)

@Composable
fun DetalleCuatrimestreScreen(
    navController: NavController,
    cuatrimestreId: Int
) {
    val tituloCuatrimestre = when (cuatrimestreId) {
        4 -> "4to Cuatrimestre"
        3 -> "3er Cuatrimestre"
        2 -> "2do Cuatrimestre"
        1 -> "1er Cuatrimestre"
        else -> "Detalle de Materias"
    }

    val materiasDelCuatrimestre = remember(cuatrimestreId) {
        getMockMateriasPorCuatrimestre(cuatrimestreId)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MockupTeal) // Usa el color compartido
                    .statusBarsPadding()
                    .height(60.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tituloCuatrimestre,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        containerColor = BackgroundColor // Usa el color compartido
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(materiasDelCuatrimestre) { materia ->
                MateriaDetailCard(materia)
            }
        }
    }
}

@Composable
fun MateriaDetailCard(materia: MockMateriaDetailed) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado
            Row(verticalAlignment = Alignment.Top) {
                // --- CORRECCIÓN 2: Cambiamos Lens por Info (Icono seguro) ---
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp).padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = materia.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    Text(text = materia.profesor, fontSize = 14.sp, color = TextGray)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

            // Detalles
            DetailRowInfo("Progreso", materia.progreso)
            DetailRowInfo("Evaluación", materia.evaluacion)

            // --- CORRECCIÓN 3: Corregimos el nombre de la variable (sin ñ) ---
            DetailRowInfo("Desempeño", materia.desempeno)

            Text(
                text = materia.estatusGeneral,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Divider(color = Color.LightGray.copy(alpha = 0.5f))

            // Unidades Temáticas
            Text(
                text = "Unidades Temáticas",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            materia.unidades.forEach { unidad ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = unidad.nombre, fontSize = 14.sp, color = TextGray, modifier = Modifier.weight(1f))
                    Text(text = unidad.estatus, fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun DetailRowInfo(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = LabelGray, fontSize = 15.sp)
        Text(text = value, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}

// MODELOS DE DATOS
data class MockUnidad(val nombre: String, val estatus: String)
data class MockMateriaDetailed(
    val nombre: String,
    val profesor: String,
    val progreso: String,
    val evaluacion: String,
    val desempeno: String, // Sin ñ
    val estatusGeneral: String,
    val unidades: List<MockUnidad>
)

// Función de datos falsos
fun getMockMateriasPorCuatrimestre(id: Int): List<MockMateriaDetailed> {
    val unidadesEstandar = listOf(
        MockUnidad("El pasado", "Estratégico"),
        MockUnidad("Pasado Simple vs. Continuo", "Por capturar"),
        MockUnidad("Verbos Irregulares", "Pendiente")
    )
    val unidadesCompletas = listOf(
        MockUnidad("Introducción", "Competente"),
        MockUnidad("Desarrollo", "Competente"),
        MockUnidad("Conclusión", "Competente")
    )

    return when (id) {
        4 -> listOf(
            MockMateriaDetailed("Inglés IV", "Lic. Marco Antonio Sólis", "50%", "Ordinaria", "--", "Por capturar", unidadesEstandar),
            MockMateriaDetailed("Ética Profesional", "Juan Ghaleb Sánchez", "100%", "Ordinaria", "E", "Estratégico", unidadesCompletas),
            MockMateriaDetailed("Desarrollo Móvil", "Ing. Pedro Pérez", "40%", "Ordinaria", "--", "En curso", unidadesEstandar),
            MockMateriaDetailed("Base de Datos II", "Dra. Ana López", "60%", "Ordinaria", "MB", "Autónomo", unidadesEstandar),
            MockMateriaDetailed("Integradora I", "Varios Docentes", "20%", "Proyecto", "--", "Inicial", unidadesEstandar)
        )
        3 -> listOf(
            MockMateriaDetailed("Inglés III", "Lic. Marco Antonio Suarez", "100%", "Ordinaria", "E", "Competente", unidadesCompletas),
            MockMateriaDetailed("Programación Web", "Ing. Roberto Ruiz", "100%", "Ordinaria", "MB", "Competente", unidadesCompletas),
            MockMateriaDetailed("Base de Datos I", "Dra. Ana López", "100%", "Ordinaria", "E", "Competente", unidadesCompletas),
            MockMateriaDetailed("Redes Básicas", "Ing. Carlos Slim", "100%", "Ordinaria", "B", "Suficiente", unidadesCompletas),
            MockMateriaDetailed("Matemáticas III", "Lic. Físico Torres", "100%", "Ordinaria", "MB", "Competente", unidadesCompletas)
        )
        else -> listOf(
            MockMateriaDetailed("Materia Genérica 1", "Profesor X", "100%", "Ordinaria", "E", "Competente", unidadesCompletas),
            MockMateriaDetailed("Materia Genérica 2", "Profesor Y", "100%", "Ordinaria", "MB", "Competente", unidadesCompletas),
            MockMateriaDetailed("Materia Genérica 3", "Profesor Z", "100%", "Ordinaria", "B", "Suficiente", unidadesCompletas),
            MockMateriaDetailed("Materia Genérica 4", "Profesor A", "100%", "Ordinaria", "E", "Competente", unidadesCompletas),
            MockMateriaDetailed("Materia Genérica 5", "Profesor B", "100%", "Ordinaria", "MB", "Competente", unidadesCompletas)
        )
    }
}