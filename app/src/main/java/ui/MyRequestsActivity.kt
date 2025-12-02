package com.example.bicireparoapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.databinding.ActivityMyRequestsBinding
import com.example.bicireparoapp.viewmodel.SolicitudViewModel
import com.example.bicireparoapp.viewmodel.SolicitudViewModelFactory

class MyRequestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyRequestsBinding
    private lateinit var adapter: SolicitudAdapter

    private val viewModel: SolicitudViewModel by viewModels {
        SolicitudViewModelFactory((application as BiciReparoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener { finish() }

        adapter = SolicitudAdapter(emptyList()) { solicitud ->
            // Opción para eliminar
            viewModel.delete(solicitud)
        }

        binding.requestsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.requestsRecyclerView.adapter = adapter

        // Recuperamos el email de la sesión actual
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val myEmail = sharedPrefs.getString("USER_EMAIL", "") ?: ""

        // DEBUG: Esto te mostrará en pantalla con qué email está filtrando
        // (Puedes borrar esta línea después de verificar que funciona)
        // Toast.makeText(this, "Cargando solicitudes de: $myEmail", Toast.LENGTH_SHORT).show()

        if (myEmail.isNotEmpty()) {
            viewModel.getSolicitudesPorUsuario(myEmail).observe(this, Observer { solicitudes ->
                if (solicitudes.isEmpty()) {
                    binding.requestsRecyclerView.visibility = View.GONE
                    binding.emptyListTextView.text = "No tienes solicitudes registradas con $myEmail"
                    binding.emptyListTextView.visibility = View.VISIBLE
                } else {
                    binding.requestsRecyclerView.visibility = View.VISIBLE
                    binding.emptyListTextView.visibility = View.GONE
                    adapter.actualizarLista(solicitudes)
                }
            })
        } else {
            // Si no hay email, mostramos vacío
            binding.requestsRecyclerView.visibility = View.GONE
            binding.emptyListTextView.text = "Error de sesión: No se encontró email"
            binding.emptyListTextView.visibility = View.VISIBLE
        }
    }
}