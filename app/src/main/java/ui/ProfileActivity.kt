package com.example.bicireparoapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.bicireparoapp.databinding.ActivityProfileBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var currentUserEmail: String = ""

    // Variable global mutable (puede ser nula)
    private var photoUri: Uri? = null

    // 1. Lanzador para la CÁMARA
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            // Usamos ?.let para asegurar que photoUri no sea null aquí
            photoUri?.let { uriSegura ->
                binding.profileImageView.setImageURI(uriSegura)
                guardarFotoDePerfil(uriSegura.toString())
            }
        } else {
            Toast.makeText(this, "No se tomó la foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener { finish() }

        cargarDatosUsuario()

        binding.changePhotoButton.setOnClickListener {
            abrirCamara()
        }

        binding.logoutButton.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun abrirCamara() {
        try {
            val photoFile = crearArchivoDeImagen()

            // Generamos la URI
            val uriTemporal = FileProvider.getUriForFile(
                this,
                "com.example.bicireparoapp.fileprovider",
                photoFile
            )

            // 1. Guardamos en la variable global (para usarla después en el callback)
            photoUri = uriTemporal

            // 2. Usamos la variable LOCAL 'uriTemporal' para lanzar la cámara
            // Al ser 'val' y local, Kotlin sabe que NO puede cambiar a null, así que no da error.
            takePictureLauncher.launch(uriTemporal)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al iniciar cámara: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun crearArchivoDeImagen(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun cargarDatosUsuario() {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val name = sharedPrefs.getString("USER_NAME", "Usuario")
        currentUserEmail = sharedPrefs.getString("USER_EMAIL", "") ?: ""
        val role = sharedPrefs.getString("USER_ROLE", "Cliente")

        binding.nameTextView.text = name
        binding.emailTextView.text = currentUserEmail
        binding.roleTextView.text = role?.uppercase()

        val photoKey = "PROFILE_IMAGE_$currentUserEmail"
        val photoUriString = sharedPrefs.getString(photoKey, null)

        if (photoUriString != null) {
            try {
                binding.profileImageView.setImageURI(Uri.parse(photoUriString))
            } catch (e: Exception) {
                // Error al cargar imagen, se mantiene la de defecto
            }
        }
    }

    private fun guardarFotoDePerfil(uriString: String) {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val photoKey = "PROFILE_IMAGE_$currentUserEmail"
        editor.putString(photoKey, uriString)
        editor.apply()
        Toast.makeText(this, "Foto guardada correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun cerrarSesion() {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            remove("USER_NAME")
            remove("USER_ROLE")
            remove("USER_EMAIL")
            apply()
        }

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}