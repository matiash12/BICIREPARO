package com.example.bicireparoapp.model

// La respuesta de la API tiene esta forma
data class IndicadoresResponse(
    val version: String,
    val autor: String,
    val fecha: String,
    val dolar: Indicador
)

data class Indicador(
    val codigo: String,
    val nombre: String,
    val unidad_medida: String,
    val fecha: String,
    val valor: Double // Este es el precio del dólar
)