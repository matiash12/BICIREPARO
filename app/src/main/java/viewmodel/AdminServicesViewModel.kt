package com.example.bicireparoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bicireparoapp.data.BiciRepository
import com.example.bicireparoapp.model.Servicio
import kotlinx.coroutines.launch

class AdminServicesViewModel(private val repository: BiciRepository) : ViewModel() {

    // Necesitamos agregar estas funciones al Repositorio primero,
    // pero las definimos aquí asumiendo que las crearemos.

    val allServices: LiveData<List<Servicio>> = repository.allServicios

    fun insert(servicio: Servicio) = viewModelScope.launch {
        repository.insertServicio(servicio)
    }

    fun delete(servicio: Servicio) = viewModelScope.launch {
        repository.deleteServicio(servicio)
    }

    fun update(servicio: Servicio) = viewModelScope.launch {
        repository.updateServicio(servicio)
    }
}

class AdminServicesViewModelFactory(private val repository: BiciRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminServicesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminServicesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}