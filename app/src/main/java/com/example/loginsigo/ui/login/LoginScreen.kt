package com.example.loginsigo.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginsigo.R
import com.example.loginsigo.data.model.UserResponse

// Tu color exacto
val SigoTurquesa = Color(0xFF2DBDA8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToWelcome: (UserResponse) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Mensajes de error/éxito
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess && uiState.user != null) {
            onNavigateToWelcome(uiState.user!!)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // --- LOGO (CAMBIO REALIZADO) ---
        // Aquí mostramos tu imagen 'utm_logo' en lugar del texto
        Image(
            painter = painterResource(id = R.drawable.logo_utm),
            contentDescription = "Logo UTM",
            contentScale = ContentScale.Fit, // Ajusta la imagen sin deformarla
            modifier = Modifier
                .width(280.dp) // Un buen tamaño para el logo
                .padding(bottom = 48.dp) // Espacio antes de los campos
        )

        // --- CAMPO USUARIO ---
        TextField(
            value = uiState.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Usuario", color = Color.Gray, fontSize = 14.sp) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = SigoTurquesa,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = SigoTurquesa
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO CONTRASEÑA ---
        TextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña", color = Color.Gray, fontSize = 14.sp) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = SigoTurquesa,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = SigoTurquesa
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // --- OLVIDASTE CONTRASEÑA ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = SigoTurquesa,
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- BOTÓN INGRESAR ---
        if (uiState.isLoading) {
            CircularProgressIndicator(color = SigoTurquesa)
        } else {
            Button(
                onClick = viewModel::login,
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SigoTurquesa)
            ) {
                Text(text = "Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}