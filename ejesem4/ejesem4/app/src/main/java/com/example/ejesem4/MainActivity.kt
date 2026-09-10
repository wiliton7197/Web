
package com.example.ejesem4

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : Activity() {

    // Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    // Último documento cargado
    private var ultimoDocumento: DocumentSnapshot? = null

    // Cantidad de posts por página
    private val cantidadPosts = 5

    // Contenedor de los posts
    private lateinit var contenedorPosts: LinearLayout

    // Botón cargar más
    private lateinit var btnCargarMas: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout principal
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(20, 20, 20, 20)

        // Título
        val titulo = TextView(this)
        titulo.text = "Lista de Posts"
        titulo.textSize = 24f
        titulo.gravity = Gravity.CENTER
        titulo.setPadding(0, 0, 0, 20)

        // Contenedor de posts
        contenedorPosts = LinearLayout(this)
        contenedorPosts.orientation = LinearLayout.VERTICAL

        // Botón
        btnCargarMas = Button(this)
        btnCargarMas.text = "Cargar más"

        // Agregar elementos
        layout.addView(titulo)
        layout.addView(contenedorPosts)

        layout.addView(
            btnCargarMas,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(layout)

        // Cargar los primeros 5 posts
        cargarNuevosPosts()

        // Botón Cargar más
        btnCargarMas.setOnClickListener {
            cargarMasPosts()
        }
    }

    // Cargar los primeros 5 posts
    private fun cargarNuevosPosts() {

        db.collection("posts")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .limit(cantidadPosts.toLong())
            .get()
            .addOnSuccessListener { resultado ->

                contenedorPosts.removeAllViews()

                if (resultado.isEmpty) {
                    mostrarMensaje("No hay posts disponibles")
                    return@addOnSuccessListener
                }

                for (documento in resultado.documents) {

                    val texto = documento.getString("texto") ?: ""

                    agregarPost(texto)
                }

                // Guardamos el último post
                ultimoDocumento = resultado.documents.last()
            }
            .addOnFailureListener { error ->

                mostrarMensaje("Error: ${error.message}")
            }
    }

    // Cargar los siguientes 5 posts
    private fun cargarMasPosts() {

        if (ultimoDocumento == null) {
            return
        }

        db.collection("posts")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .startAfter(ultimoDocumento!!)
            .limit(cantidadPosts.toLong())
            .get()
            .addOnSuccessListener { resultado ->

                if (resultado.isEmpty) {

                    mostrarMensaje("No hay más posts")

                    btnCargarMas.isEnabled = false

                    return@addOnSuccessListener
                }

                for (documento in resultado.documents) {

                    val texto = documento.getString("texto") ?: ""

                    agregarPost(texto)
                }

                // Actualizamos el último documento
                ultimoDocumento = resultado.documents.last()
            }
            .addOnFailureListener { error ->

                mostrarMensaje("Error: ${error.message}")
            }
    }

    // Mostrar un post
    private fun agregarPost(texto: String) {

        val post = TextView(this)

        post.text = texto
        post.textSize = 18f

        post.setPadding(
            20,
            20,
            20,
            20
        )

        contenedorPosts.addView(post)
    }

    // Mostrar mensajes
    private fun mostrarMensaje(mensaje: String) {

        val texto = TextView(this)

        texto.text = mensaje
        texto.textSize = 16f
        texto.setPadding(10, 20, 10, 20)

        contenedorPosts.addView(texto)
    }
}

