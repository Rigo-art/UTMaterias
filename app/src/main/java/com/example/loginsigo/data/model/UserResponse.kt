package com.example.loginsigo.data.model

import com.google.gson.annotations.SerializedName

// Quitamos @Serializable porque estamos usando Gson en Retrofit
data class UserResponse(
    @SerializedName("termsConditions") val termsConditions: Boolean,
    @SerializedName("registerUser") val registerUser: String,
    @SerializedName("active") val active: Boolean,
    @SerializedName("messageControl") val messageControl: String,
    @SerializedName("accessModule") val accessModule: String,
    @SerializedName("personFullName") val personFullName: String,
    @SerializedName("personId") val personId: Int,
    @SerializedName("register") val register: String,
    @SerializedName("profileName") val profileName: String,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("roles") val roles: List<String>,

    // --- CORRECCIÓN CLAVE ---
    // Mantenemos el nombre "bearer" para que tu AuthRepository compile bien.
    // Agregamos 'alternate' por si el servidor lo envía como "token" o "access_token".
    @SerializedName(value = "bearer", alternate = ["token", "access_token", "id_token"])
    val bearer: String
)