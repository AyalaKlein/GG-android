package com.example.gg.data

import com.example.gg.data.dataSource.LoginDataSource
import com.example.gg.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        dataSource.login(username, password) { result ->
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }

            callback(result)
        }
    }

    fun register(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        dataSource.register(username, password) { result ->
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }

            callback(result)
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
