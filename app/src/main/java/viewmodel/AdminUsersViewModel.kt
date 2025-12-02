package com.example.bicireparoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bicireparoapp.model.UsuarioResponse
import com.example.bicireparoapp.network.RetrofitClient
import kotlinx.coroutines.launch

class AdminUsersViewModel : ViewModel() {

    // Lista de usuarios que viene del Servidor
    private val _usuarios = MutableLiveData<List<UsuarioResponse>>()
    val usuarios: LiveData<List<UsuarioResponse>> = _usuarios

    // Mensajes para mostrar en Toast (ej: "Usuario eliminado", "Error de conexión")
    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    // 1. CARGAR LISTA (GET)
    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userInstance.getUsuarios()
                if (response.isSuccessful) {
                    _usuarios.value = response.body() ?: emptyList()
                } else {
                    _mensaje.value = "Error al cargar: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error de conexión: ${e.message}"
            }
        }
    }

    // 2. CREAR USUARIO (POST) - Reutilizamos el endpoint de registro
    fun crearUsuario(nombre: String, email: String, pass: String, rol: String) {
        viewModelScope.launch {
            try {
                val datos = mapOf(
                    "nombre" to nombre,
                    "email" to email,
                    "password" to pass,
                    "rol" to rol
                )
                val response = RetrofitClient.userInstance.registrar(datos)
                if (response.isSuccessful) {
                    _mensaje.value = "Usuario creado correctamente"
                    cargarUsuarios() // Recargar la lista para ver al nuevo
                } else {
                    _mensaje.value = "Error al crear: ¿Email duplicado?"
                }
            } catch (e: Exception) {
                _mensaje.value = "Fallo de conexión: ${e.message}"
            }
        }
    }

    // 3. EDITAR USUARIO (PUT)
    fun editarUsuario(id: Long, nombre: String, email: String, pass: String, rol: String) {
        viewModelScope.launch {
            try {
                val datos = mapOf(
                    "nombre" to nombre,
                    "email" to email,
                    "password" to pass,
                    "rol" to rol
                )
                val response = RetrofitClient.userInstance.actualizarUsuario(id, datos)

                if (response.isSuccessful) {
                    _mensaje.value = "Usuario actualizado"
                    cargarUsuarios() // Recargar lista
                } else {
                    _mensaje.value = "No se pudo actualizar"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.message}"
            }
        }
    }

    // 4. ELIMINAR USUARIO (DELETE)
    fun eliminarUsuario(id: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.userInstance.eliminarUsuario(id)
                if (response.isSuccessful) {
                    _mensaje.value = "Usuario eliminado"
                    cargarUsuarios() // Recargar lista (el usuario desaparecerá)
                } else {
                    _mensaje.value = "Error al eliminar"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.message}"
            }
        }
    }
}