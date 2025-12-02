package com.example.bicireparoapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bicireparoapp.databinding.ActivityAdminUsersBinding
import com.example.bicireparoapp.model.UsuarioResponse
import com.example.bicireparoapp.viewmodel.AdminUsersViewModel

class AdminUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUsersBinding
    private lateinit var adapter: UserAdapter

    // YA NO NECESITAMOS FACTORY (porque no usa repository local)
    private val viewModel: AdminUsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        setupRecyclerView()

        binding.addUserFab.setOnClickListener {
            startActivity(Intent(this, AdminUserEditActivity::class.java))
        }

        // Observamos MENSAJES (Ej: "Usuario eliminado")
        viewModel.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Cargar la lista actualizada cada vez que entramos a la pantalla
        viewModel.cargarUsuarios()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(
            emptyList(),
            onEditClick = { usuario ->
                val intent = Intent(this, AdminUserEditActivity::class.java)
                intent.putExtra("EXTRA_ID", usuario.id)
                intent.putExtra("EXTRA_NOMBRE", usuario.nombre)
                intent.putExtra("EXTRA_EMAIL", usuario.email)
                // Nota: La contraseña no suele venir del servidor por seguridad,
                // así que la dejamos vacía o la manejamos diferente.
                intent.putExtra("EXTRA_ROL", usuario.rol)
                startActivity(intent)
            },
            onDeleteClick = { usuario ->
                confirmarEliminacion(usuario)
            }
        )

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usersRecyclerView.adapter = adapter

        // Observamos la LISTA que viene del servidor
        viewModel.usuarios.observe(this) { usuarios ->
            adapter.actualizarLista(usuarios)
        }
    }

    private fun confirmarEliminacion(usuario: UsuarioResponse) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Estás seguro de eliminar a ${usuario.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                // Llamada al servidor para borrar por ID
                viewModel.eliminarUsuario(usuario.id)
            }
            .setNegativeButton("No", null)
            .show()
    }
}