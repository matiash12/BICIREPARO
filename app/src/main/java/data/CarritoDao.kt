package com.example.bicireparoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bicireparoapp.model.Carrito

@Dao
interface CarritoDao {
    // Ver qué hay en el carrito
    @Query("SELECT * FROM carrito_compras")
    fun obtenerCarrito(): LiveData<List<Carrito>>

    // Calcular el total a pagar (Suma de precios)
    @Query("SELECT SUM(precio) FROM carrito_compras")
    fun obtenerTotal(): LiveData<Int>

    // Añadir al carrito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: Carrito)

    // Eliminar un ítem del carrito
    @Delete
    suspend fun eliminar(item: Carrito)

    // Vaciar el carrito (al comprar)
    @Query("DELETE FROM carrito_compras")
    suspend fun vaciarCarrito()
}