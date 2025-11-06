package com.example.bicireparoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityPopularServicesBinding
import com.example.bicireparoapp.model.Servicio

class PopularServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularServicesBinding

    // 1. Declarar el Adapter
    private lateinit var servicioAdapter: ServicioAdapter

    // 2. Lista de servicios (la escribimos aquí mismo)
    private val listaDeServicios = listOf(
        Servicio("Ajuste de Cambios", "Regulación de desviadores delantero y trasero.", "$10.000"),
        Servicio("Ajuste de Frenos", "Calibración y limpieza de frenos (V-Brake o disco).", "$10.000"),
        Servicio("Inflado de Ruedas", "Revisión e inflado de neumáticos a presión correcta.", "$2.000"),
        Servicio("Parche de Rueda", "Reparación de pinchazo (no incluye cámara nueva).", "$5.000"),
        Servicio("Lubricación de Cadena", "Limpieza y lubricación profesional de transmisión.", "$7.000"),
        Servicio("Mantención Básica", "Ajuste de cambios, frenos y lubricación.", "$25.000"),
        Servicio("Centrado de Rueda", "Alineación y tensado de rayos (por rueda).", "$12.000")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón Volver
        binding.backTextView.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_left)
        }

        // 3. Configurar el RecyclerView
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // Inicializamos el adapter y le pasamos la lista de servicios
        servicioAdapter = ServicioAdapter(listaDeServicios)

        // Asignamos el adapter a nuestro RecyclerView
        binding.servicesRecyclerView.adapter = servicioAdapter
    }
}