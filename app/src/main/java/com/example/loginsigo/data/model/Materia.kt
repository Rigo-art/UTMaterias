package com.example.loginsigo.data.model

data class Materia(
    val id: Int,
    val nombre: String,
    val calificacion: String,
    val ciclo: String,
    val estatus: String // Cambiamos a String para poder escribir "Aprobada" o "Reprobada"
)