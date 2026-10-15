
package com.example.ejesem9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var nombre by remember { mutableStateOf("") }
            var personas by remember { mutableIntStateOf(0) }
            var ultimo by remember { mutableStateOf("Ninguno") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(text = "Control de aforo")

                Text(text = "Personas adentro: $personas")

                Text(text = "Último registrado: $ultimo")

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del invitado") }
                )

                Button(
                    onClick = {
                        if (nombre.isNotBlank()) {
                            ultimo = nombre
                            personas += 1
                            nombre = ""
                        }
                    }
                ) {
                    Text("Registrar")
                }

                Row {

                    Button(
                        enabled = personas > 0,
                        onClick = {
                            if (personas > 0) {
                                personas -= 1
                            }
                        }
                    ) {
                        Text("Salida -1")
                    }

                    Button(
                        onClick = {
                            personas = 0
                            ultimo = "Ninguno"
                            nombre = ""
                        }
                    ) {
                        Text("Reiniciar")
                    }
                }
            }
        }
    }
}
