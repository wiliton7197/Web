package com.example.mostrarlista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                UserListScreen()
            }
        }
    }
}

@Composable
fun UserListScreen() {
    var usuarios by remember { mutableStateOf(emptyList<Usuario>()) }

    LaunchedEffect(Unit) {
        try {
            val resultado = RetrofitInstance.api.getUsuarios()
            usuarios = resultado
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(usuarios) { usuario ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = usuario.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = usuario.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}