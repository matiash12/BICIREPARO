package com.example.bicireparoapp.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager // Importante
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.databinding.ActivityAdminRequestsBinding
import com.example.bicireparoapp.viewmodel.SolicitudViewModel
import com.example.bicireparoapp.viewmodel.SolicitudViewModelFactory

class AdminRequestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminRequestsBinding
    private lateinit var adapter: SolicitudAdapter // Asegúrate de haber creado este archivo

    private val viewModel: SolicitudViewModel by viewModels {
        SolicitudViewModelFactory((application as BiciReparoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener { finish() }

        // Configurar RecyclerView
        adapter = SolicitudAdapter(emptyList()) { solicitud ->
            viewModel.delete(solicitud)
        }

        binding.requestsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.requestsRecyclerView.adapter = adapter

        // Observar datos
        viewModel.allSolicitudes.observe(this, Observer { solicitudes ->
            if (solicitudes.isEmpty()) {
                binding.requestsRecyclerView.visibility = View.GONE
                binding.emptyListTextView.visibility = View.VISIBLE
            } else {
                binding.requestsRecyclerView.visibility = View.VISIBLE
                binding.emptyListTextView.visibility = View.GONE
                adapter.actualizarLista(solicitudes) // Usamos el método correcto del adapter
            }
        })
    }
}