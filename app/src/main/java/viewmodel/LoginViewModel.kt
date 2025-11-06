package com.example.bicireparoapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Usamos AndroidViewModel para poder acceder a SharedPreferences
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Definimos los posibles estados del login
    enum class LoginState {
        SUCCESS, // Éxito
        INVALID_CREDENTIALS, // Email o contraseña incorrectos
        USER_NOT_FOUND // No hay ningún usuario registrado
    }

    // LiveData para notificar a la Activity sobre el estado del login
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    /**
     * Esta es la función que la Activity llamará al presionar "Ingresar".
     */
    fun login(emailIngresado: String, passwordIngresada: String) {

        // 1. Validaciones básicas de campos vacíos (opcional pero recomendado)
        if (emailIngresado.isEmpty() || passwordIngresada.isEmpty()) {
            _loginState.value = LoginState.INVALID_CREDENTIALS
            return
        }

        // 2. Acceder a SharedPreferences para leer los datos guardados
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)

        // 3. Obtener los datos del registro
        // Si no encuentra nada, usa "null" como valor por defecto
        val emailGuardado = sharedPrefs.getString("USER_EMAIL", null)
        val passwordGuardada = sharedPrefs.getString("USER_PASSWORD", null)

        // 4. Lógica de verificación
        if (emailGuardado == null) {
            // Aún no se ha registrado nadie
            _loginState.value = LoginState.USER_NOT_FOUND
        } else if (emailIngresado == emailGuardado && passwordIngresada == passwordGuardada) {
            // ¡Éxito! Las credenciales coinciden
            _loginState.value = LoginState.SUCCESS
        } else {
            // El email o la contraseña son incorrectos
            _loginState.value = LoginState.INVALID_CREDENTIALS
        }
    }
}