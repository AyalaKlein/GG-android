package com.example.gg.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.gg.data.LoginRepository
import com.example.gg.data.Result

import com.example.gg.R
import com.example.gg.data.model.LoggedInUser

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password) { result ->
            if (result is Result.Success) {
                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }

            callback(result)
        }
    }

    fun register(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        loginRepository.register(username, password) { result ->
            if (result is Result.Success) {
                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            } else {
                _loginResult.value = LoginResult(error = R.string.register_failed)
            }

            callback(result)
        }
    }

    fun logout(){
        loginRepository.logout()
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
