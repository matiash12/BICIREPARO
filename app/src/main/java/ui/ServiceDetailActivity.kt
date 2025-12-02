package com.example.bicireparoapp.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityServiceDetailBinding
import com.example.bicireparoapp.viewmodel.SolicitudViewModel
import com.example.bicireparoapp.viewmodel.SolicitudViewModelFactory

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceDetailBinding

    private val viewModel: SolicitudViewModel by viewModels {
        SolicitudViewModelFactory((application as BiciReparoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Recibir datos
        val nombre = intent.getStringExtra("EXTRA_NOMBRE") ?: "Servicio"
        val descripcion = intent.getStringExtra("EXTRA_DESC") ?: "Sin descripción"
        val precio = intent.getIntExtra("EXTRA_PRECIO", 0)
        val fotoUriString = intent.getStringExtra("EXTRA_FOTO")

        // 2. Mostrar datos
        binding.detailNameText.text = nombre
        binding.detailDescText.text = descripcion
        binding.detailPriceText.text = "$ $precio"

        // 3. Cargar imagen con seguridad
        if (!fotoUriString.isNullOrEmpty()) {
            try {
                binding.detailImageView.setImageURI(Uri.parse(fotoUriString))
            } catch (e: Exception) {
                binding.detailImageView.setImageResource(R.drawable.ic_popular)
            }
        } else {
            binding.detailImageView.setImageResource(R.drawable.ic_popular)
        }

        binding.backButton.setOnClickListener { finish() }

        // 4. Botón Añadir al Carrito
        binding.addToCartButton.setOnClickListener {
            // IMPORTANTE: Si esto sale en rojo, revisa tu SolicitudViewModel
            viewModel.agregarAlCarrito(nombre, precio, fotoUriString)

            Toast.makeText(this, "Añadido al carrito", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}