package com.example.bicireparoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bicireparoapp.data.BiciRepository
import com.example.bicireparoapp.model.Carrito
import com.example.bicireparoapp.model.Solicitud
import kotlinx.coroutines.launch

class SolicitudViewModel(private val repository: BiciRepository) : ViewModel() {

    // ADMIN: Ve todas
    val allSolicitudes: LiveData<List<Solicitud>> = repository.allSolicitudes

    // CLIENTE: Ve solo las suyas (¡NUEVO!)
    fun getSolicitudesPorUsuario(email: String): LiveData<List<Solicitud>> {
        return repository.getSolicitudesPorUsuario(email)
    }

    fun insert(solicitud: Solicitud) = viewModelScope.launch {
        repository.insertSolicitud(solicitud)
    }

    fun delete(solicitud: Solicitud) = viewModelScope.launch {
        repository.deleteSolicitud(solicitud)
    }

    // --- Carrito ---
    val itemsCarrito: LiveData<List<Carrito>> = repository.itemsCarrito
    val totalCarrito: LiveData<Int> = repository.totalCarrito

    fun agregarAlCarrito(nombre: String, precio: Int, fotoUri: String?) = viewModelScope.launch {
        val nuevoItem = Carrito(
            nombreServicio = nombre,
            precio = precio,
            fotoUri = fotoUri
        )
        repository.agregarAlCarrito(nuevoItem)
    }

    fun eliminarDelCarrito(item: Carrito) = viewModelScope.launch {
        repository.eliminarDelCarrito(item)
    }

    fun comprarYVaciar() = viewModelScope.launch {
        repository.vaciarCarrito()
    }
}

class SolicitudViewModelFactory(private val repository: BiciRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolicitudViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolicitudViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}