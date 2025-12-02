package com.example.bicireparoapp

import android.app.Application
import com.example.bicireparoapp.data.AppDatabase
import com.example.bicireparoapp.data.BiciRepository
import com.example.bicireparoapp.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BiciReparoApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy {
        BiciRepository(
            database.solicitudDao(),
            database.carritoDao(),
            database.usuarioDao(),
            database.servicioDao()
        )
    }

    override fun onCreate() {
        super.onCreate()

        // Usamos IO para que NO se congele la pantalla al iniciar
        CoroutineScope(Dispatchers.IO).launch {
            val admin = Usuario(
                nombre = "Administrador",
                email = "admin@bicireparo.com",
                password = "admin",
                rol = "admin"
            )
            repository.insertUsuario(admin)
        }
    }
}