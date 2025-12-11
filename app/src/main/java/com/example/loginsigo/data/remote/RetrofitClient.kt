package com.example.loginsigo.data.network // O com.example.loginsigo.data.remote, según tu carpeta real

import com.example.loginsigo.data.remote.ApiService // Asegúrate de que este import coincida con donde guardaste ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit configurado para conectar con el servidor SIGO/Prometheus de la UTM.
 */
object RetrofitClient {

    // CAMBIO CLAVE:
    // 1. Usamos la IP (189.206.96.198) para conexión directa.
    // 2. Agregamos "/ws/rest/" porque ahí viven todos los servicios (Login, Kardex, etc).
    // NOTA: Asegúrate de que termina con una barra "/"
    private const val BASE_URL = "http://189.206.96.198:8080/ws/rest/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}