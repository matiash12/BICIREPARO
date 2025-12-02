package com.example.bicireparoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bicireparoapp.model.UsuarioResponse
import com.example.bicireparoapp.network.UserApi // Importamos la interfaz
import kotlinx.coroutines.launch
import retrofit2.Response

// CAMBIO: Ahora recibimos 'api' en el constructor
class LoginViewModel(application: Application, private val api: UserApi) : AndroidViewModel(application) {

    // --- LOGIN ---
    private val _loginResult = MutableLiveData<Result<UsuarioResponse>>()
    val loginResult: LiveData<Result<UsuarioResponse>> = _loginResult

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            try {
                val credenciales = mapOf("email" to email, "password" to pass)
                // Usamos 'api' (que viene del constructor) en lugar de RetrofitClient directo
                val response = api.login(credenciales)

                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = Result.success(response.body()!!)
                } else {
                    _loginResult.value = Result.failure(Exception("Credenciales incorrectas"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    // --- REGISTRO ---
    private val _registerResult = MutableLiveData<Result<UsuarioResponse>>()
    val registerResult: LiveData<Result<UsuarioResponse>> = _registerResult

    fun registrar(nombre: String, email: String, pass: String, rol: String) {
        viewModelScope.launch {
            try {
                val datosUsuario = mapOf(
                    "nombre" to nombre,
                    "email" to email,
                    "password" to pass,
                    "rol" to rol
                )
                // Usamos 'api'
                val response = api.registrar(datosUsuario)

                if (response.isSuccessful && response.body() != null) {
                    _registerResult.value = Result.success(response.body()!!)
                } else {
                    _registerResult.value = Result.failure(Exception("Error al registrar"))
                }
            } catch (e: Exception) {
                _registerResult.value = Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }
}

// FACTORY: Necesaria para pasarle la API al ViewModel
class LoginViewModelFactory(
    private val application: Application,
    private val api: UserApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application, api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}