package com.example.bicireparoapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels // ¡Importante!
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer // ¡Importante!
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityRegisterBinding
import com.example.bicireparoapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // 1. Obtenemos una instancia de nuestro ViewModel
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Configuramos el botón de volver
        binding.backTextView.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_left)
        }

        // 3. Configuramos el botón de registrarse
        binding.registerButton.setOnClickListener {
            // 3.1 Obtenemos los textos de los campos
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            // 3.2 Le pasamos los datos al "cerebro" (ViewModel) para que él los valide
            viewModel.validateForm(name, email, password, confirmPassword)
        }

        // 4.
        // La Activity se queda "escuchando" los cambios que el ViewModel publica.

        viewModel.nameError.observe(this, Observer { error ->
            binding.nameInputLayout.error = error
        })

        viewModel.emailError.observe(this, Observer { error ->
            binding.emailInputLayout.error = error
        })

        viewModel.passwordError.observe(this, Observer { error ->
            binding.passwordInputLayout.error = error
        })

        viewModel.confirmPasswordError.observe(this, Observer { error ->
            binding.confirmPasswordInputLayout.error = error
        })

        viewModel.registrationSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show()

                // Navegamos a la pantalla principal
                val intent = android.content.Intent(this, HomeActivity::class.java)
                // Estas "flags" limpian las pantallas anteriores (Login/Registro)
                // para que el usuario no pueda volver a ellas con el botón "atrás".
                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish() // Cierra RegisterActivity
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay_still)
            }
        })
    }
}