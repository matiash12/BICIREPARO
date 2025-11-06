package com.example.bicireparoapp.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityMyRequestsBinding
import com.example.bicireparoapp.model.Solicitud
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyRequestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyRequestsBinding
    private lateinit var solicitudAdapter: SolicitudAdapter
    private var requestsList: ArrayList<Solicitud> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón Volver
        binding.backTextView.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_left)
        }

        // Configurar el RecyclerView y el Adapter
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // Cargar los datos cada vez que la pantalla se muestra
        loadAndDisplayRequests()
    }

    private fun setupRecyclerView() {
        solicitudAdapter = SolicitudAdapter { solicitudParaEliminar ->
            // Esto se ejecutará cuando el usuario presione 'X' en un ítem
            handleDeleteSolicitud(solicitudParaEliminar)
        }

        binding.requestsRecyclerView.adapter = solicitudAdapter
    }

    private fun loadAndDisplayRequests() {
        try {
            val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonRequests = sharedPrefs.getString("USER_REQUESTS", null)
            val type = object : TypeToken<ArrayList<Solicitud>>() {}.type

            requestsList = if (jsonRequests != null) {
                gson.fromJson(jsonRequests, type) ?: ArrayList()
            } else {
                ArrayList()
            }

            if (requestsList.isEmpty()) {
                binding.requestsRecyclerView.visibility = View.GONE
                binding.emptyListTextView.visibility = View.VISIBLE
            } else {
                binding.requestsRecyclerView.visibility = View.VISIBLE
                binding.emptyListTextView.visibility = View.GONE

                // Enviar la lista (ordenada de más nueva a más vieja)
                solicitudAdapter.submitList(requestsList.sortedByDescending { it.id })
            }
        } catch (e: Exception) {
            binding.emptyListTextView.text = "Error al cargar las solicitudes."
            binding.emptyListTextView.visibility = View.VISIBLE
            binding.requestsRecyclerView.visibility = View.GONE
            e.printStackTrace()
        }
    }

    /**
     * Esta función maneja la lógica de eliminación de una solicitud.
     */
    private fun handleDeleteSolicitud(solicitud: Solicitud) {
        try {
            // Eliminar el ítem de nuestra lista local
            requestsList.removeAll { it.id == solicitud.id }

            // Guardar la lista MODIFICADA de vuelta en SharedPreferences
            val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
            val gson = Gson()
            with(sharedPrefs.edit()) {
                val updatedJsonRequests = gson.toJson(requestsList)
                putString("USER_REQUESTS", updatedJsonRequests)
                apply()
            }

            // Volver a cargar los datos en el RecyclerView
            loadAndDisplayRequests()

        } catch (e: Exception) {
            Toast.makeText(this, "Error al eliminar la solicitud", Toast.LENGTH_SHORT).show() // <-- Aquí estaba el error
            e.printStackTrace()
        }
    }
}