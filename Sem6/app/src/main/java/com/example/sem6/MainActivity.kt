package com.example.sem6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.sem6.ui.theme.Sem6Theme

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Agotado : Screen("agotado")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sem6Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            PantallaPrincipal(navController)
        }

        composable(Screen.Agotado.route) {
            PantallaAgotado()
        }
    }
}

@Composable
fun PantallaPrincipal(navController: NavHostController) {

    var cantidadProducto by remember { mutableStateOf(5) }

    Column(modifier = Modifier.padding(25.dp)) {


        Text(text = "Cantidad: $cantidadProducto")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (cantidadProducto > 0) {
                cantidadProducto--
            }

            if (cantidadProducto == 0) {
                navController.navigate(Screen.Agotado.route)
            }
        }) {
            Text("Vender Unidad")
        }
    }
}

@Composable
fun PantallaAgotado() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No quedan existencias disponibles")
    }
}