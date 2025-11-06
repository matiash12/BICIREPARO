package com.example.bicireparoapp.model

/**
 * Esta es una "data class" (clase de datos).
 * Es una plantilla simple para guardar la información de una solicitud.
 */
data class Solicitud(
    val id: Long, // Un identificador único (usaremos la fecha en milisegundos)
    val descripcion: String,
    val fecha: String, // La fecha formateada (ej. "01/11/2025")
    val fotoUri: String? // La ruta a la foto (puede ser nula si no se tomó foto)
)