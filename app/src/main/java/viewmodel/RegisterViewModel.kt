package com.example.bicireparoapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    // --- LiveData ---
    private val _nameError = MutableLiveData<String?>()
    private val _emailError = MutableLiveData<String?>()
    private val _passwordError = MutableLiveData<String?>()
    private val _confirmPasswordError = MutableLiveData<String?>()
    private val _registrationSuccess = MutableLiveData<Boolean>()

    val nameError: LiveData<String?> = _nameError
    val emailError: LiveData<String?> = _emailError
    val passwordError: LiveData<String?> = _passwordError
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    /**
     * CAMBIO 1: La función ahora se llama 'saveUser'
     * y acepta los 3 datos.
     */
    private fun saveUser(name: String, email: String, pass: String) {
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)

        with(sharedPrefs.edit()) {
            putString("USER_NAME", name)
            // CAMBIO 2: Guardamos también el email y la contraseña
            putString("USER_EMAIL", email)
            putString("USER_PASSWORD", pass)
            apply()
        }
    }

    /**
     * Función de validación (Casi igual que antes)
     */
    fun validateForm(name: String, email: String, pass: String, confirmPass: String) {
        // --- (Toda la lógica de validación de campos queda exactamente igual) ---
        _nameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null

        var isValid = true

        if (name.isEmpty()) {
            _nameError.value = "El nombre es obligatorio"
            isValid = false
        }
        if (email.isEmpty()) {
            _emailError.value = "El email es obligatorio"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "El formato del email no es válido"
            isValid = false
        }
        if (pass.isEmpty()) {
            _passwordError.value = "La contraseña es obligatoria"
            isValid = false
        } else if (pass.length < 6) {
            _passwordError.value = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }
        if (confirmPass.isEmpty()) {
            _confirmPasswordError.value = "Confirma tu contraseña"
            isValid = false
        } else if (pass != confirmPass) {
            _confirmPasswordError.value = "Las contraseñas no coinciden"
            isValid = false
        }
        // --- (Fin de la lógica de validación) ---

        if (isValid) {
            // CAMBIO 3: Llamamos a la nueva función con los 3 datos
            saveUser(name, email, pass)
            _registrationSuccess.value = true
        }
    }
}