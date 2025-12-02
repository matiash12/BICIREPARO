package com.example.bicireparoapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.databinding.ActivityLoginBinding
import com.example.bicireparoapp.network.RetrofitClient // Importamos el cliente
import com.example.bicireparoapp.viewmodel.LoginViewModel
import com.example.bicireparoapp.viewmodel.LoginViewModelFactory // Importamos la Factory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // CAMBIO CLAVE: Usamos la Factory para pasarle la API real (Puerto 8080)
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(application, RetrofitClient.userInstance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Botón Login con VALIDACIÓN (Mantenemos tu validación de 6 caracteres)
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese el email", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Conectando con el servidor...", Toast.LENGTH_SHORT).show()
                viewModel.login(email, password)
            }
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 2. Observer
        viewModel.loginResult.observe(this) { result ->
            if (result.isSuccess) {
                val usuario = result.getOrNull()

                guardarSesion(
                    nombre = usuario?.nombre ?: "",
                    rol = usuario?.rol ?: "cliente",
                    email = usuario?.email ?: ""
                )

                Toast.makeText(this, "Bienvenido ${usuario?.nombre}", Toast.LENGTH_SHORT).show()
                irAHome()

            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun guardarSesion(nombre: String, rol: String, email: String) {
        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString("USER_NAME", nombre)
            putString("USER_ROLE", rol)
            putString("USER_EMAIL", email)
            apply()
        }
    }

    private fun irAHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}