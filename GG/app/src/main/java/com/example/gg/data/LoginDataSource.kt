package com.example.gg.data

import com.example.gg.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.Success<LoggedInUser>(LoggedInUser(auth.currentUser!!.uid, username)))
                } else {
                    callback(Result.Error(IOException("Failed to login user", task.exception)))
                }
            }
    }

    fun register(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Result.Success<LoggedInUser>(LoggedInUser(auth.currentUser!!.uid, username)))
                } else {
                    callback(Result.Error(IOException("Failed to register user", task.exception)))
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}

