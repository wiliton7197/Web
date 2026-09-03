package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
// MODELO
data class Equipo(
    val id: String? = null,
    val nombre: String,
    val pais: String
)// API
interface EquipoApi {
    @GET("equipos")
    suspend fun obtenerEquipos(): List<Equipo>

    @POST("equipos")
    suspend fun crearEquipo(
        @Body equipo: Equipo
    ): Equipo
    @PUT("equipos/{id}")
    suspend fun actualizarEquipo(
        @Path("id") id: String,
        @Body equipo: Equipo
    ): Equipo

    @DELETE("equipos/{id}")
    suspend fun eliminarEquipo(
        @Path("id") id: String
    ): Response<Unit>
}
// R
object RetrofitInstance {
    private const val BASE_URL =
        "https://69e1842eb1cb62b9f316e8cf.mockapi.io/"
    //api es nuestra instacia de equipos
    val api: EquipoApi by lazy {
        //ic
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(EquipoApi::class.java)
    }
}
// MAIN ACTIVITY
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                //mp
                CrudEquipos()
            }
        }
    }
}
// CRUD
@androidx.compose.runtime.Composable
fun CrudEquipos() {
    // Lista que viene desde MockAPI
    var equipos by remember {
        mutableStateOf<List<Equipo>>(emptyList())
    }
    // Campo nombre
    var nombre by remember {
        mutableStateOf("")
    }
    // Campo país
    var pais by remember {
        mutableStateOf("")
    }
    // Equipo seleccionado para editar
    var equipoEditando by remember {
        mutableStateOf<Equipo?>(null)
    }
    // Permite ejecutar las peticiones
    val scope = rememberCoroutineScope()
    // GET
    fun obtenerEquipos() {
        scope.launch {
            try {
                equipos = RetrofitInstance.api.obtenerEquipos()
            } catch (e: Exception) {
                println("ERROR GET: ${e.message}")
            }
        }
    }
    // POST
    fun crearEquipo() {
        if (nombre.isBlank() || pais.isBlank()) {
            return
        }
        scope.launch {
            try {
                val equipo = Equipo(
                    nombre = nombre,
                    pais = pais
                )
                RetrofitInstance.api.crearEquipo(equipo)
                // Lf
                nombre = ""
                pais = ""
                // acLis
                obtenerEquipos()
            } catch (e: Exception) {
                println("ERROR POST: ${e.message}")
            }
        }
    }
    // PUT
    fun actualizarEquipo() {
        val equipo = equipoEditando ?: return
        scope.launch {
            try {
                val equipoActualizado = Equipo(
                    id = equipo.id,
                    nombre = nombre,
                    pais = pais
                )
                RetrofitInstance.api.actualizarEquipo(
                    equipo.id!!,
                    equipoActualizado
                )
                // Limpiar formulario
                nombre = ""
                pais = ""
                // Salir del modo edición
                equipoEditando = null
                // Actualizar lista
                obtenerEquipos()
            } catch (e: Exception) {
                println("ERROR PUT: ${e.message}")
            }
        }
    }
    // DELETE
    fun eliminarEquipo(id: String) {
        scope.launch {
            try {
                RetrofitInstance.api.eliminarEquipo(id)
                // Aclis
                obtenerEquipos()
            } catch (e: Exception) {
                println("ERROR DELETE: ${e.message}")
            }
        }
    }
    // CARGAR DATOS DE MOCKAPI y se cargue
    LaunchedEffect(Unit) {
        obtenerEquipos()
    }
    // INTERFAZ
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //T
        Text(
            text = "CRUD EQUIPOS DE FÚTBOL"
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        // LISTA DE DATOS DE LA API
        Text(
            text = "Equipos registrados en MockAPI"
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(equipos) { equipo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // ID
                    Text(
                        text = "ID: ${equipo.id}"
                    )
                    // NOMBRE
                    Text(
                        text = "Equipo: ${equipo.nombre}"
                    )
                    // PAÍS
                    Text(
                        text = "País: ${equipo.pais}"
                    )
                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceEvenly
                    ) {
                   // EDITAR
                        Button(
                            onClick = {
                                equipoEditando = equipo
                                nombre = equipo.nombre
                                pais = equipo.pais
                            }
                        ) {
                            Text("EDITAR")
                        }
                        // ELIMINAR
                        Button(
                            onClick = {
                                equipo.id?.let {
                                    eliminarEquipo(it)
                                }
                            }
                        ) {
                            Text("ELIMINAR")
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        // FORMULARIO
        Text(
            text =
                if (equipoEditando == null)
                    "Registrar nuevo equipo"
                else
                    "Editar equipo"
        )
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        // NOMBRE
         OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            label = {
                Text("Nombre del equipo")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        // PAÍS
        OutlinedTextField(
            value = pais,
            onValueChange = {
                pais = it
            },
            label = {
                Text("País")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        // BOTÓN
        Button(
            onClick = {
                if (equipoEditando == null) {
                    crearEquipo()
                } else {
                    actualizarEquipo()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (equipoEditando == null)
                    "CREAR EQUIPO"
                else
                    "ACTUALIZAR EQUIPO"
            )
        }
    }
}
