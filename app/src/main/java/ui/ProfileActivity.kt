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
import androidx.core.net.toUri
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityProfileBinding
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var latestTmpUri: Uri? = null

    // Lanzador para tomar la foto (reutilizado de NewRequestActivity)
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.profileImageView.setImageURI(uri)
                // Guardamos la URI de la foto en SharedPreferences
                saveProfilePhotoUri(uri)
            }
        }
    }

    // Lanzador para pedir permiso de cámara (reutilizado)
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar botón Volver
        binding.backTextView.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_left)
        }

        // Configurar botón Cambiar Foto
        binding.changePhotoButton.setOnClickListener {
            checkCameraPermission()
        }

        // Cargar los datos del perfil al iniciar
        loadProfileData()
    }

    private fun loadProfileData() {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)

        // 1. Cargar Nombre y Email
        val name = sharedPrefs.getString("USER_NAME", "No registrado")
        val email = sharedPrefs.getString("USER_EMAIL", "No registrado")
        binding.profileNameTextView.text = name
        binding.profileEmailTextView.text = email

        // 2. Cargar Foto de Perfil
        val photoUriString = sharedPrefs.getString("PROFILE_PHOTO_URI", null)
        if (photoUriString != null) {
            try {
                // Convertimos el String de vuelta a Uri y lo mostramos
                binding.profileImageView.setImageURI(photoUriString.toUri())
            } catch (e: Exception) {
                // Si la URI está corrupta o el archivo se borró
                binding.profileImageView.setImageResource(android.R.drawable.ic_menu_camera)
            }
        }
    }

    private fun saveProfilePhotoUri(uri: Uri) {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            // Guardamos la URI como un String
            putString("PROFILE_PHOTO_URI", uri.toString())
            apply()
        }
        Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
    }

    // --- Lógica de Cámara (Copiada y pegada de NewRequestActivity) ---

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCamera() {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    private fun getTmpFileUri(): Uri {
        // Usamos un nombre de archivo específico para la foto de perfil
        val tmpFile = File(cacheDir, "profile_image.png").apply {
            createNewFile()
            deleteOnExit() // Opcional: puedes quitar esto si quieres que persista entre reinicios de la app
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.fileprovider",
            tmpFile
        )
    }
}