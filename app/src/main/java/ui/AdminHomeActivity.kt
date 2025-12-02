package com.example.bicireparoapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.databinding.ActivityAdminHomeBinding

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Botón GESTIONAR USUARIOS (Conectado a AdminUsersActivity)
        binding.cardManageUsers.setOnClickListener {
            startActivity(Intent(this, AdminUsersActivity::class.java))
        }

        // 2. Botón GESTIONAR SERVICIOS
        binding.cardManageServices.setOnClickListener {
            startActivity(Intent(this, AdminServicesActivity::class.java))
        }

        // 3. Botón TODAS LAS SOLICITUDES
        binding.cardAllRequests.setOnClickListener {
            startActivity(Intent(this, AdminRequestsActivity::class.java))
        }

        // 4. Botón CERRAR SESIÓN
        binding.cardLogout.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        // Borramos solo la sesión actual para no perder fotos de otros usuarios
        with(sharedPrefs.edit()) {
            remove("USER_NAME")
            remove("USER_ROLE")
            remove("USER_EMAIL")
            apply()
        }

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}