package com.example.gg.data.dataSource

import com.example.gg.data.Result
import com.example.gg.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        FireBaseDataSource.Auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(
                        Result.Success<LoggedInUser>(
                            LoggedInUser(FireBaseDataSource.Auth.currentUser!!.uid, username)
                        )
                    )
                } else {
                    callback(
                        Result.Error(
                            "Failed to login user"
                        )
                    )
                }
            }
    }

    fun register(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        FireBaseDataSource.Auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(
                        Result.Success<LoggedInUser>(
                            LoggedInUser(FireBaseDataSource.Auth.currentUser!!.uid, username)
                        )
                    )
                } else {
                    callback(
                        Result.Error(
                            "Failed to register user"
                        )
                    )
                }
            }
    }

    fun logout() {
        FireBaseDataSource.Auth.signOut()
    }
}

