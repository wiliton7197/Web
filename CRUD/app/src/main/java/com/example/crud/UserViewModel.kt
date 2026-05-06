package com.example.crud

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        getUsers()
    }

    private fun getUsers() {
        usersCollection.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            if (value != null) {
                _users.value = value.toObjects<User>()
            }
        }
    }

    fun addUser(name: String, email: String) {
        val id = usersCollection.document().id
        val user = User(id, name, email)
        usersCollection.document(id).set(user)
    }

    fun updateUser(user: User) {
        usersCollection.document(user.id).set(user)
    }

    fun deleteUser(userId: String) {
        usersCollection.document(userId).delete()
    }
}
