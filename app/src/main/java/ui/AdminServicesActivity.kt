package com.example.bicireparoapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ActivityAdminServicesBinding
import com.example.bicireparoapp.model.Servicio
import com.example.bicireparoapp.viewmodel.AdminServicesViewModel
import com.example.bicireparoapp.viewmodel.AdminServicesViewModelFactory

class AdminServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminServicesBinding
    private lateinit var adapter: ServicioAdapter

    private val viewModel: AdminServicesViewModel by viewModels {
        AdminServicesViewModelFactory((application as BiciReparoApplication).repository)
    }

    // Variable temporal para guardar la URI seleccionada en el diálogo
    private var tempSelectedImageUri: Uri? = null
    // Referencia temporal al ImageView del diálogo para actualizarlo
    private var tempDialogImageView: ImageView? = null

    // Lanzador para seleccionar imagen de la galería
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // ¡IMPORTANTE! Tomar permisos persistentes para acceder a la foto siempre
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    e.printStackTrace() // Manejar error si no se puede tomar permiso
                }

                tempSelectedImageUri = uri
                // Actualizamos la vista previa en el diálogo
                tempDialogImageView?.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener { finish() }

        adapter = ServicioAdapter(emptyList()) { servicio ->
            mostrarDialogoOpciones(servicio)
        }
        binding.servicesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.servicesRecyclerView.adapter = adapter

        viewModel.allServices.observe(this, Observer { servicios ->
            adapter.actualizarLista(servicios)
        })

        binding.fabAddService.setOnClickListener {
            mostrarDialogoGuardar(null)
        }
    }

    private fun mostrarDialogoGuardar(servicio: Servicio?) {
        // Reseteamos variables temporales
        tempSelectedImageUri = servicio?.fotoUri?.let { Uri.parse(it) }
        tempDialogImageView = null

        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (servicio == null) "Nuevo Servicio" else "Editar Servicio")

        // --- Creamos el Layout del Diálogo dinámicamente ---
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Vista previa de la imagen
        tempDialogImageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(200, 200).apply {
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                bottomMargin = 24
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundColor(android.graphics.Color.LTGRAY)
            // Si estamos editando y tiene foto, la mostramos
            if (tempSelectedImageUri != null) {
                setImageURI(tempSelectedImageUri)
            } else {
                setImageResource(R.drawable.ic_popular) // Icono por defecto
            }
        }
        layout.addView(tempDialogImageView)

        // Botón para seleccionar foto
        val btnSelectPhoto = Button(this).apply {
            text = "Seleccionar Foto"
            setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                pickImageLauncher.launch(intent)
            }
        }
        layout.addView(btnSelectPhoto)

        // Campos de texto
        val inputNombre = EditText(this)
        inputNombre.hint = "Nombre del Servicio"
        inputNombre.setText(servicio?.nombre)
        layout.addView(inputNombre)

        val inputDesc = EditText(this)
        inputDesc.hint = "Descripción"
        inputDesc.setText(servicio?.descripcion)
        layout.addView(inputDesc)

        val inputPrecio = EditText(this)
        inputPrecio.hint = "Precio (solo números)"
        inputPrecio.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        inputPrecio.setText(servicio?.precio?.toString() ?: "")
        layout.addView(inputPrecio)

        builder.setView(layout)
        // ---------------------------------------------------

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = inputNombre.text.toString()
            val desc = inputDesc.text.toString()
            val precioString = inputPrecio.text.toString()

            if (nombre.isNotBlank() && precioString.isNotBlank()) {
                val precioInt = precioString.toIntOrNull() ?: 0

                val nuevoServicio = Servicio(
                    id = servicio?.id ?: 0L,
                    nombre = nombre,
                    descripcion = desc,
                    precio = precioInt,
                    fotoUri = tempSelectedImageUri?.toString() // Guardamos la URI como texto
                )

                if (servicio == null) {
                    viewModel.insert(nuevoServicio)
                } else {
                    viewModel.update(nuevoServicio)
                }
                Toast.makeText(this, "Servicio Guardado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Faltan datos (Nombre y Precio son obligatorios)", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun mostrarDialogoOpciones(servicio: Servicio) {
        val opciones = arrayOf("Editar", "Eliminar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(servicio.nombre)
        builder.setItems(opciones) { _, which ->
            when (which) {
                0 -> mostrarDialogoGuardar(servicio)
                1 -> confirmarEliminar(servicio)
            }
        }
        builder.show()
    }

    private fun confirmarEliminar(servicio: Servicio) {
        AlertDialog.Builder(this)
            .setTitle("¿Eliminar ${servicio.nombre}?")
            .setMessage("Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.delete(servicio)
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}