package com.example.loginsigo.data.remote

import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
// Asegúrate de importar el modelo que creaste (Materia o MateriaItemResponse)
import com.example.loginsigo.data.model.Materia
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Header

interface ApiService {
    @POST("auth")
    suspend fun loginUser(@Body request: LoginRequest): Response<UserResponse>

    // CAMBIO CLAVE:
    // 1. Agregamos @Header("Authorization") para enviar el token.
    // 2. Quitamos el {id} de la URL. Probamos directo a "kardex".
    //    Si falla, probaremos "alumno/kardex".
    @GET("kardex")
    suspend fun getHistorial(
        @Header("Authorization") token: String
    ): Response<List<Materia>>
}