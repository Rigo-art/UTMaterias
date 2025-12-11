package com.example.loginsigo.data

import com.example.loginsigo.data.local.TokenManager
import com.example.loginsigo.data.model.LoginRequest
import com.example.loginsigo.data.model.UserResponse
import com.example.loginsigo.data.network.RetrofitClient // Asegúrate de que este import sea correcto
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val tokenManager: TokenManager
) {
    // Usamos el cliente que ya configuramos
    private val apiService = RetrofitClient.apiService

    suspend fun login(request: LoginRequest): Result<UserResponse> {
        return try {
            val response = apiService.loginUser(request)

            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!

                // *** CORRECCIÓN FINAL AQUÍ ***
                // La variable en tu UserResponse se llama 'bearer', así que la llamamos así.
                // Como definiste que es String (no nulo), la usamos directo.
                val tokenRecibido = userResponse.bearer

                tokenManager.saveToken(tokenRecibido)

                Result.success(userResponse)
            } else {
                Result.failure(Exception("Error al iniciar sesión: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSavedToken(): Flow<String?> = tokenManager.getToken()

    suspend fun logout() {
        tokenManager.clearToken()
    }
}