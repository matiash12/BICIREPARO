package com.example.bicireparoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_compras")
data class Carrito(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombreServicio: String,
    val precio: Int,
    val fotoUri: String? = null // <--- ¡NUEVO CAMPO!
)