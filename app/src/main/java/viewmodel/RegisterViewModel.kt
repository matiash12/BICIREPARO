package com.example.bicireparoapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bicireparoapp.data.BiciRepository
import com.example.bicireparoapp.model.Usuario
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: BiciRepository) : ViewModel() {

    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> = _nameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> = _confirmPasswordError

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    fun validateForm(name: String, email: String, pass: String, confirmPass: String) {
        // Limpiar errores
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
            _emailError.value = "Email inválido"
            isValid = false
        }
        if (pass.isEmpty()) {
            _passwordError.value = "La contraseña es obligatoria"
            isValid = false
        } else if (pass.length < 6) {
            _passwordError.value = "Mínimo 6 caracteres"
            isValid = false
        }
        if (confirmPass.isEmpty()) {
            _confirmPasswordError.value = "Confirma tu contraseña"
            isValid = false
        } else if (pass != confirmPass) {
            _confirmPasswordError.value = "Las contraseñas no coinciden"
            isValid = false
        }

        if (isValid) {
            // Verificar si ya existe el usuario antes de insertar
            viewModelScope.launch {
                val existe = repository.buscarUsuarioPorEmail(email)
                if (existe != null) {
                    _emailError.value = "Este email ya está registrado"
                } else {
                    // Crear y guardar usuario
                    val nuevoUsuario = Usuario(
                        nombre = name,
                        email = email,
                        password = pass,
                        rol = "cliente" // Por defecto es cliente
                    )
                    repository.insertUsuario(nuevoUsuario)
                    _registrationSuccess.value = true
                }
            }
        }
    }
}

class RegisterViewModelFactory(private val repository: BiciRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}