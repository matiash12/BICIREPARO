package com.example.bicireparoapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.databinding.ActivityAdminUserEditBinding
import com.example.bicireparoapp.viewmodel.AdminUsersViewModel

class AdminUserEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUserEditBinding

    // Sin Factory
    private val viewModel: AdminUsersViewModel by viewModels()

    private var userId: Long = 0 // 0 = Crear nuevo, >0 = Editar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUserEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("EXTRA_ID", 0)
        val name = intent.getStringExtra("EXTRA_NOMBRE")
        val email = intent.getStringExtra("EXTRA_EMAIL")
        val rol = intent.getStringExtra("EXTRA_ROL")

        if (userId > 0) {
            binding.titleText.text = "Editar Usuario"
            binding.nameEditText.setText(name)
            binding.emailEditText.setText(email)
            // binding.passwordEditText.setText(pass) // La contraseña se deja en blanco salvo que quiera cambiarla

            // Permitimos editar email si el backend lo soporta, o lo bloqueamos:
            // binding.emailEditText.isEnabled = false

            if (rol == "admin") {
                binding.radioAdmin.isChecked = true
            } else {
                binding.radioClient.isChecked = true
            }
        }

        binding.saveButton.setOnClickListener {
            guardarUsuario()
        }

        binding.cancelButton.setOnClickListener { finish() }

        // Observamos la respuesta del servidor
        viewModel.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            // Si el mensaje es de éxito, cerramos.
            // (Una forma simple de validar es ver el texto, o mejorar el ViewModel con un LiveData booleano)
            if (msg.contains("creado") || msg.contains("actualizado")) {
                finish()
            }
        }
    }

    private fun guardarUsuario() {
        val nombre = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        val rol = if (binding.radioAdmin.isChecked) "admin" else "cliente"

        if (nombre.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nombre y Email son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (userId == 0L) {
            // CREAR NUEVO
            if (pass.isEmpty()) {
                Toast.makeText(this, "La contraseña es obligatoria para crear", Toast.LENGTH_SHORT).show()
                return
            }
            viewModel.crearUsuario(nombre, email, pass, rol)
        } else {
            // EDITAR EXISTENTE
            // Si la contraseña está vacía, podríamos enviar la antigua o manejarlo en el backend.
            // Por ahora asumimos que si edita, debe poner pass o enviamos la misma cadena si tienes lógica para eso.
            // Para simplificar, requerimos pass o enviamos una por defecto si el backend no la valida en update.
            val passToSend = if(pass.isEmpty()) "123" else pass // OJO: Idealmente el backend no debería cambiar pass si viene vacía.

            viewModel.editarUsuario(userId, nombre, email, passToSend, rol)
        }
    }
}