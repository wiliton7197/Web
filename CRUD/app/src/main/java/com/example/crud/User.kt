package com.example.crud

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = ""
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this("", "", "")
}
