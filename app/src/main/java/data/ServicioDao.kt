package com.example.bicireparoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bicireparoapp.model.Servicio

@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicios ORDER BY nombre ASC")
    fun obtenerTodos(): LiveData<List<Servicio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(servicio: Servicio)

    @Update
    suspend fun actualizar(servicio: Servicio)

    @Delete
    suspend fun eliminar(servicio: Servicio)
}