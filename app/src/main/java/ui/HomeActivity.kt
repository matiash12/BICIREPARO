package com.example.bicireparoapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Cargar datos del usuario (Nombre, Rol y Foto)
        setupUserHeader()

        // 2. Listeners de las Tarjetas (Dashboards)
        setupDashboard()

        // 3. Listener Botón Admin (Tu código original)
        binding.btnAdminPanel.setOnClickListener {
            startActivity(Intent(this, AdminHomeActivity::class.java))
        }

        // 4. Listener Foto de Perfil (Para ir al perfil)
        binding.profileImageView.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargamos por si cambió la foto
        setupUserHeader()
    }

    private fun setupUserHeader() {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("USER_NAME", "Ciclista")
        val userRole = sharedPrefs.getString("USER_ROLE", "cliente")
        val userEmail = sharedPrefs.getString("USER_EMAIL", "") ?: ""

        // Texto de bienvenida
        binding.welcomeTextView.text = "Hola, $userName"

        // LÓGICA DE VISIBILIDAD ADMIN (Tu código original)
        if (userRole == "admin") {
            binding.btnAdminPanel.visibility = View.VISIBLE
        } else {
            binding.btnAdminPanel.visibility = View.GONE
        }

        // LÓGICA DE FOTO DE PERFIL (Nuevo: Cargar por email único)
        val photoKey = "PROFILE_IMAGE_$userEmail"
        val photoUriString = sharedPrefs.getString(photoKey, null)

        if (photoUriString != null) {
            try {
                binding.profileImageView.setImageURI(Uri.parse(photoUriString))
            } catch (e: Exception) {
                // Fallo al cargar, usar default
                binding.profileImageView.setImageResource(R.drawable.ic_popular)
            }
        } else {
            // Si no hay foto, usar default
            binding.profileImageView.setImageResource(R.drawable.ic_popular)
        }
    }

    private fun setupDashboard() {
        binding.cardNewRequest.setOnClickListener {
            startActivity(Intent(this, NewRequestActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        binding.cardServices.setOnClickListener {
            startActivity(Intent(this, PopularServicesActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        // Usamos el ID cardHistory que tienes en tu XML original
        binding.cardHistory.setOnClickListener {
            startActivity(Intent(this, MyRequestsActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        binding.cardCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}