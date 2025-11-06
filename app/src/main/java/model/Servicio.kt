package com.example.bicireparoapp.model

/**
 * Data class para representar un servicio predefinido.
 * Incluye un nombre, una descripción simple y un precio.
 */
data class Servicio(
    val nombre: String,
    val descripcion: String,
    val precio: String // Usamos String para el precio para incluir el signo "$"
)