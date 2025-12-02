package com.example.bicireparoapp.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.databinding.ActivityNewRequestBinding
import com.example.bicireparoapp.model.Solicitud
import com.example.bicireparoapp.viewmodel.SolicitudViewModel
import com.example.bicireparoapp.viewmodel.SolicitudViewModelFactory
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewRequestBinding
    private var selectedImageUri: Uri? = null

    private val viewModel: SolicitudViewModel by viewModels {
        SolicitudViewModelFactory((application as BiciReparoApplication).repository)
    }

    // Cliente GPS
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    // Permiso GPS
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            obtenerUbicacionActual()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Selector de Imagen
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                binding.previewImageView.setImageURI(imageUri)
                try {
                    contentResolver.takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener { finish() }

        val services = listOf("Mantención General", "Reparación Frenos", "Ajuste Cambios", "Pinchazo", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, services)
        binding.serviceTypeAutoComplete.setAdapter(adapter)

        // Botón GPS
        binding.gpsButton.setOnClickListener {
            checkLocationPermission()
        }

        binding.uploadPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        binding.submitButton.setOnClickListener {
            crearSolicitud()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            obtenerUbicacionActual()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun obtenerUbicacionActual() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val coordenadas = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                    binding.locationEditText.setText(coordenadas)
                    Toast.makeText(this, "Ubicación obtenida", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Activa el GPS", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun crearSolicitud() {
        val tipoServicio = binding.serviceTypeAutoComplete.text.toString()
        val descripcionInput = binding.descriptionEditText.text.toString()
        val ubicacion = binding.locationEditText.text.toString()

        if (tipoServicio.isEmpty() || descripcionInput.isEmpty() || ubicacion.isEmpty()) {
            Toast.makeText(this, "Completa descripción y ubicación", Toast.LENGTH_SHORT).show()
            return
        }

        // Unimos la ubicación a la descripción para guardarla
        val descripcionFinal = "Ubicación: $ubicacion\n\nDetalle: $descripcionInput"

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("USER_EMAIL", "invitado@bicireparo.com") ?: "invitado@bicireparo.com"

        val nuevaSolicitud = Solicitud(
            descripcion = descripcionFinal,
            fecha = fechaActual,
            fotoUri = selectedImageUri?.toString(),
            tipoServicio = tipoServicio,
            precioEstimado = 0,
            usuarioEmail = userEmail
        )

        viewModel.insert(nuevaSolicitud)

        Toast.makeText(this, "Solicitud enviada", Toast.LENGTH_SHORT).show()
        finish()
    }
}