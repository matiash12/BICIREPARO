package com.example.bicireparoapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityNewRequestBinding
import com.example.bicireparoapp.model.Solicitud
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewRequestBinding
    private var latestTmpUri: Uri? = null

    // --- Lanzadores ---
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) { getLocation() } else {
            Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_LONG).show()
        }
    }
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.photoPreviewImageView.setImageURI(uri)
            }
        }
    }
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) { launchCamera() } else {
            Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Configuración de Botones ---
        binding.backTextView.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_left)
        }
        binding.takePhotoButton.setOnClickListener {
            checkCameraPermission()
        }
        binding.getLocationButton.setOnClickListener {
            checkLocationPermission()
        }

        binding.sendRequestButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString().trim()
            val locationText = binding.locationTextView.text.toString()

            // --- Validaciones ---
            if (description.isBlank()) {
                binding.descriptionInputLayout.error = "La descripción es obligatoria"
                return@setOnClickListener
            } else {
                binding.descriptionInputLayout.error = null
            }
            if (locationText == getString(R.string.location_placeholder)) {
                Toast.makeText(this, "Por favor, registra tu ubicación", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (latestTmpUri == null) {
                Toast.makeText(this, "Por favor, toma una foto del problema", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // --- Fin Validaciones ---

            // --- Lógica de Guardado ---
            try {
                val fechaActual = Date()
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                val nuevaSolicitud = Solicitud(
                    id = fechaActual.time,
                    descripcion = description,
                    fecha = formatoFecha.format(fechaActual),
                    fotoUri = latestTmpUri.toString()
                )

                val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
                val gson = Gson()
                val jsonRequests = sharedPrefs.getString("USER_REQUESTS", null)
                val type = object : TypeToken<ArrayList<Solicitud>>() {}.type
                val requestsList: ArrayList<Solicitud> = if (jsonRequests != null) {
                    gson.fromJson(jsonRequests, type) ?: ArrayList()
                } else {
                    ArrayList()
                }
                requestsList.add(nuevaSolicitud)

                with(sharedPrefs.edit()) {
                    val updatedJsonRequests = gson.toJson(requestsList)
                    putString("USER_REQUESTS", updatedJsonRequests)
                    apply()
                }

                Toast.makeText(this, "Solicitud enviada y guardada", Toast.LENGTH_LONG).show()
                finish()
                overridePendingTransition(R.anim.stay_still, R.anim.slide_out_left)

            } catch (e: Exception) {
                Toast.makeText(this, "Error al guardar la solicitud", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
            // --- Fin Lógica de Guardado ---
        }
    }

    // --- Lógica de GPS  ---
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> { getLocation() }
            else -> { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
        }
    }
    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        binding.locationTextView.text = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                    } else {
                        binding.locationTextView.text = "Ubicación no disponible. Activa el GPS."
                    }
                }
        }
    }

    // --- Lógica de Cámara ---
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> { launchCamera() }
            else -> { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
        }
    }
    private fun launchCamera() {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takePictureLauncher.launch(uri)
        }
    }
    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("request_image_${System.currentTimeMillis()}", ".png", cacheDir).apply {
            createNewFile()

        }
        return FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.fileprovider",
            tmpFile
        )
    }
}