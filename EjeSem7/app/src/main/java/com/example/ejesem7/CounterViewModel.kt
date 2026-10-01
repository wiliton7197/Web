package com.example.ejesem7

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CounterViewModel : ViewModel() {

    private val _contador = MutableStateFlow(0)

    val contador: StateFlow<Int> = _contador

    fun incrementar() {
        _contador.value++
    }
    fun Restar() {
        _contador.value--
    }
}