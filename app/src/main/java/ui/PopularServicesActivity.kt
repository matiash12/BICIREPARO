package com.example.bicireparoapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityPopularServicesBinding
import com.example.bicireparoapp.viewmodel.AdminServicesViewModel
import com.example.bicireparoapp.viewmodel.AdminServicesViewModelFactory

class PopularServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopularServicesBinding
    private lateinit var servicioAdapter: ServicioAdapter // Debe estar importado

    // Usamos el ViewModel de Admin para LEER los servicios
    private val servicesViewModel: AdminServicesViewModel by viewModels {
        AdminServicesViewModelFactory((application as BiciReparoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener {
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        servicioAdapter = ServicioAdapter(emptyList()) { servicio ->
            // Al hacer clic, abrimos el detalle (NO añadimos directo)
            val intent = Intent(this, ServiceDetailActivity::class.java)
            intent.putExtra("EXTRA_NOMBRE", servicio.nombre)
            intent.putExtra("EXTRA_DESC", servicio.descripcion)
            intent.putExtra("EXTRA_PRECIO", servicio.precio)
            intent.putExtra("EXTRA_FOTO", servicio.fotoUri)
            startActivity(intent)
        }

        binding.servicesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.servicesRecyclerView.adapter = servicioAdapter

        // Observar la lista de servicios desde la Base de Datos
        servicesViewModel.allServices.observe(this, Observer { servicios ->
            servicioAdapter.actualizarLista(servicios)
        })
    }
}