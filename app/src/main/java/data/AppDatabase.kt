package com.example.bicireparoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bicireparoapp.model.Carrito
import com.example.bicireparoapp.model.Servicio // Importar Servicio
import com.example.bicireparoapp.model.Solicitud
import com.example.bicireparoapp.model.Usuario

// Ahora tenemos 4 tablas: Solicitud, Carrito, Usuario y SERVICIO
@Database(entities = [Usuario::class, Servicio::class, Solicitud::class, Carrito::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun solicitudDao(): SolicitudDao
    abstract fun carritoDao(): CarritoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun servicioDao(): ServicioDao // <--- NUEVO DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bicireparo_database"
                )
                    .fallbackToDestructiveMigration() // Borra datos viejos si cambia la estructura
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}