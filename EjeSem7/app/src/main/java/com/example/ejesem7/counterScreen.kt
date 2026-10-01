package com.example.ejesem7

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()
) {
    val contador = viewModel.contador.collectAsState()
    Scaffold(

    ) {paddingValues ->
        Column (modifier = Modifier.padding(paddingValues)){

            Text(
                text = "Contador: ${contador.value}"
            )

            Button(
                onClick = {
                    viewModel.incrementar()
                }
            ) {
                Text("Incrementar")
            }
            Button(
                onClick = {
                    viewModel.Restar()
                }
            ) {
                Text("restrar")
            }
        }
    }

}