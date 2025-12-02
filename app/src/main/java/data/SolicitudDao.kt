package com.example.bicireparoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bicireparoapp.model.Carrito
import com.example.bicireparoapp.model.Servicio
import com.example.bicireparoapp.model.Solicitud
import com.example.bicireparoapp.model.Usuario

@Dao
interface SolicitudDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(solicitud: Solicitud)

    @Delete
    suspend fun delete(solicitud: Solicitud)

    // Para el ADMIN: Ve todas
    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun getAllSolicitudes(): LiveData<List<Solicitud>>

    // Para el CLIENTE: Ve solo las suyas (¡NUEVO!)
    @Query("SELECT * FROM solicitudes WHERE usuarioEmail = :email ORDER BY id DESC")
    fun getSolicitudesPorUsuario(email: String): LiveData<List<Solicitud>>
}