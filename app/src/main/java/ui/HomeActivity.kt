package com.example.bicireparoapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityHomeBinding // ¡Binding correcto!

class HomeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar el nombre de usuario
        loadUserName()

        // --- Configurar la Toolbar ---
        setupToolbar()

        // --- Listeners de los Botones (Principales) ---

        // Botón 1: Navegar a Nueva Solicitud
        binding.newRequestButton.setOnClickListener {
            val intent = Intent(this, NewRequestActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
        }

        // Botón 2: Navegar a Mis Solicitudes
        binding.myRequestsButton.setOnClickListener {
            val intent = Intent(this, MyRequestsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
        }


    }

    /**
     * Configura los listeners para los iconos de la Toolbar
     */
    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                // Caso 1: Clic en "Perfil"
                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
                    true // Indica que hemos manejado el clic
                }

                // Caso 2: Clic en "Populares"
                R.id.menu_popular -> {
                    val intent = Intent(this, PopularServicesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
                    true // Indica que hemos manejado el clic
                }

                else -> false // No hemos manejado el clic
            }
        }
    }

    /**
     * Lee el nombre de usuario desde SharedPreferences y actualiza el TextView.
     */
    private fun loadUserName() {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val userName = sharedPrefs.getString("USER_NAME", "Usuario")
        binding.welcomeTextView.text = "¡Bienvenido, $userName!"
    }
}