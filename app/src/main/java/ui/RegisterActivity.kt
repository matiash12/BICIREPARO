package com.example.bicireparoapp.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.databinding.ActivityRegisterBinding
import com.example.bicireparoapp.network.RetrofitClient // Importamos el cliente
import com.example.bicireparoapp.viewmodel.LoginViewModel
import com.example.bicireparoapp.viewmodel.LoginViewModelFactory // Importamos la Factory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // CAMBIO CLAVE: Usamos la Factory para pasarle la API real
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(application, RetrofitClient.userInstance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()

        binding.registerButton.setOnClickListener {
            val nombre = binding.nombreEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()
            val rol = binding.roleSpinner.selectedItem.toString().lowercase()

            // Tu validación de 6 caracteres ya está aquí incluida correctamente ✅
            if (validarCampos(nombre, email, pass)) {
                binding.registerButton.isEnabled = false
                binding.registerButton.text = "Registrando..."
                viewModel.registrar(nombre, email, pass, rol)
            }
        }

        viewModel.registerResult.observe(this) { result ->
            binding.registerButton.isEnabled = true
            binding.registerButton.text = "Registrar"

            if (result.isSuccess) {
                Toast.makeText(this, "¡Registro Exitoso!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupSpinner() {
        val roles = arrayOf("Cliente", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roleSpinner.adapter = adapter
    }

    private fun validarCampos(nombre: String, email: String, pass: String): Boolean {
        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.contains("@")) {
            Toast.makeText(this, "Ingrese un email válido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass.length < 6) {
            Toast.makeText(this, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}