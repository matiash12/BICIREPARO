package com.example.bicireparoapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels // ¡Importante!
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer // ¡Importante!
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityLoginBinding
import com.example.bicireparoapp.viewmodel.LoginViewModel // ¡Importar el nuevo ViewModel!

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // 1. Obtenemos una instancia de nuestro LoginViewModel
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Configuramos el botón de Ingresar
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // 3. Le pasamos los datos al ViewModel para que él valide
            viewModel.login(email, password)
        }

        // 4. Configuramos el botón de Registro
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
        }

        // 5.  Observamos el estado del login
        viewModel.loginState.observe(this, Observer { state ->
            // Reaccionamos según el estado que nos envíe el ViewModel
            when (state) {
                LoginViewModel.LoginState.SUCCESS -> {
                    // Éxito: Navegamos al Home
                    Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Cerramos el Login
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
                }
                LoginViewModel.LoginState.INVALID_CREDENTIALS -> {
                    // Error: Credenciales incorrectas
                    Toast.makeText(this, "Email o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
                LoginViewModel.LoginState.USER_NOT_FOUND -> {
                    // Error: No hay usuario
                    Toast.makeText(this, "No se encontró ningún usuario. Por favor, regístrate.", Toast.LENGTH_LONG).show()
                }
                null -> {
                    // Estado nulo, no hacer nada
                }
            }
        })
    }
}