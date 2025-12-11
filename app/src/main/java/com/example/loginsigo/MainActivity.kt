package com.example.loginsigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loginsigo.data.model.UserResponse
import com.example.loginsigo.di.LoginViewModelFactory
import com.example.loginsigo.ui.historial.DetalleCuatrimestreScreen // <--- IMPORTANTE
import com.example.loginsigo.ui.historial.HistorialScreen
import com.example.loginsigo.ui.login.LoginScreen
import com.example.loginsigo.ui.login.LoginViewModel
import com.example.loginsigo.ui.login.WelcomeScreen
import com.google.gson.Gson

object Routes {
    const val LOGIN = "login_screen"
    const val WELCOME = "welcome_screen/{userJson}"
    const val HISTORIAL = "historial_screen/{userJson}"
    // NUEVA RUTA: Espera el ID del cuatrimestre (un número entero)
    const val DETALLE_CUATRIMESTRE = "detalle_screen/{cuatrimestreId}"

    fun welcome(userJson: String) = "welcome_screen/$userJson"
    fun historial(userJson: String) = "historial_screen/$userJson"
    // Helper para navegar a la nueva ruta
    fun detalleCuatrimestre(id: Int) = "detalle_screen/$id"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppScreenEntry()
            }
        }
    }
}

@Composable
fun AppScreenEntry() {
    val appContext = LocalContext.current.applicationContext
    val application = appContext as SigoLoginApplication
    val authRepository = application.container.authRepository
    val factory = LoginViewModelFactory(authRepository)
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // 1. LOGIN
        composable(Routes.LOGIN) {
            val viewModel: LoginViewModel = viewModel(factory = factory)
            LoginScreen(
                viewModel = viewModel,
                onNavigateToWelcome = { userResponse ->
                    val userJson = Gson().toJson(userResponse)
                    navController.navigate(Routes.welcome(userJson)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 2. BIENVENIDA
        composable(
            route = Routes.WELCOME,
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson")
            val userResponse = Gson().fromJson(userJson, UserResponse::class.java)
            if (userResponse != null) {
                WelcomeScreen(user = userResponse, navController = navController)
            }
        }

        // 3. HISTORIAL PRINCIPAL
        composable(
            route = Routes.HISTORIAL,
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson")
            val userResponse = Gson().fromJson(userJson, UserResponse::class.java)
            if (userResponse != null) {
                HistorialScreen(navController = navController, user = userResponse)
            }
        }

        // 4. NUEVA PANTALLA: DETALLE DE CUATRIMESTRE
        composable(
            route = Routes.DETALLE_CUATRIMESTRE,
            arguments = listOf(navArgument("cuatrimestreId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Recuperamos el ID que enviamos
            val id = backStackEntry.arguments?.getInt("cuatrimestreId") ?: 0
            DetalleCuatrimestreScreen(navController = navController, cuatrimestreId = id)
        }
    }
}