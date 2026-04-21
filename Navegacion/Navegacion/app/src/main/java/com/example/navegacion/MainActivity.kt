package com.example.navegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navegacion.ui.theme.NavegacionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavegacionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio",
        modifier = modifier
    ) {
        // inicio
        composable("inicio") {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { navController.navigate("home") }) {
                    Text("Ir a Home")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("formulario") }) {
                    Text("Ir a Formulario")
                }
            }
        }

        // PANTALLA2
        composable("home") {
            var textoCentral by remember { mutableStateOf("Bienvenidos") }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Home") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Regresar"
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = textoCentral == "Bienvenidoooo",
                            onClick = { textoCentral = "Bienvenidooooo" },
                            label = { Text("") },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) }
                        )
                        NavigationBarItem(
                            selected = textoCentral == "¡INGRESAMOS XDE!",
                            onClick = { textoCentral = "¡INGRESAMOS XDE!" },
                            label = { Text("") },
                            icon = { Icon(Icons.Default.Star, contentDescription = null) }
                        )
                    }
                }
            ) { paddingInterno ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingInterno),
                    contentAlignment = Alignment.Center
                ) {
                    Text(textoCentral, style = MaterialTheme.typography.displaySmall)
                }
            }
        }

        // FORMULARIO
        composable("formulario") {
            var nombre by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (nombre.isNotBlank()) navController.navigate("resultado/$nombre")
                }) {
                    Text("Enviar")
                }
            }
        }

        // FINAL
        composable(
            route = "resultado/{nombreUsuario}",
            arguments = listOf(navArgument("nombreUsuario") { defaultValue = "" })
        ) { backStackEntry ->
            val nombreExtraido = backStackEntry.arguments?.getString("nombreUsuario")

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Resultado") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null)
                            }
                        }
                    )
                }
            ) { p ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(p),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "HOLIIIII: $nombreExtraido", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}