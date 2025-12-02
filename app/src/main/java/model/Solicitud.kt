package com.example.bicireparoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes") // Esto crea una tabla SQL llamada "solicitudes"
data class Solicitud(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // ID automático
    val descripcion: String,
    val fecha: String,
    val fotoUri: String?, // Guardamos la ruta de la foto
    val tipoServicio: String? = "Reparación General", // Nuevo campo para ser más completo
    val precioEstimado: Int = 0,
    val usuarioEmail: String
)