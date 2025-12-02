package com.example.bicireparoapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope // Importante para corrutinas
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.databinding.ActivityCheckoutBinding
import com.example.bicireparoapp.model.Carrito
import com.example.bicireparoapp.model.Solicitud
import com.example.bicireparoapp.network.RetrofitClient // Importante para la API
import com.example.bicireparoapp.viewmodel.SolicitudViewModel
import com.example.bicireparoapp.viewmodel.SolicitudViewModelFactory
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers // Importante para hilos
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private val viewModel: SolicitudViewModel by viewModels {
        SolicitudViewModelFactory((application as BiciReparoApplication).repository)
    }

    private var totalAmount = 0
    private var cartItems: List<Carrito> = emptyList()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            obtenerUbicacionActual()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTextView.setOnClickListener { finish() }

        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPrefs.getString("USER_NAME", "")
        binding.nameEditText.setText(savedName)

        // Observamos el total del carrito
        viewModel.totalCarrito.observe(this, Observer { total ->
            totalAmount = total ?: 0
            binding.totalAmountText.text = "$ $totalAmount"

            // --- NUEVO: LLAMADA A LA API ---
            // Cada vez que cambia el total, calculamos su valor en dólares
            if (totalAmount > 0) {
                calcularDolar(totalAmount)
            }
        })

        viewModel.itemsCarrito.observe(this, Observer { items ->
            cartItems = items
        })

        binding.confirmButton.setOnClickListener {
            procesarCompra()
        }

        binding.gpsButton.setOnClickListener {
            checkLocationPermission()
        }
    }

    // --- NUEVA FUNCIÓN: Consumo de API en segundo plano ---
    private fun calcularDolar(montoPesos: Int) {
        lifecycleScope.launch(Dispatchers.IO) { // Hilo secundario (IO)
            try {
                // Hacemos la petición a Internet
                val response = RetrofitClient.currencyInstance.getIndicadores()

                if (response.isSuccessful) {
                    val indicadores = response.body()
                    val valorDolar = indicadores?.dolar?.valor ?: 0.0

                    if (valorDolar > 0) {
                        val totalDolares = montoPesos / valorDolar

                        // Volvemos al Hilo Principal (Main) para tocar la UI
                        withContext(Dispatchers.Main) {
                            // Actualizamos el botón con el precio en USD
                            binding.confirmButton.text = "Pagar ($ ${String.format("%.2f", totalDolares)} USD)"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si falla (sin internet), no hacemos nada o mostramos error en Log
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            obtenerUbicacionActual()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun obtenerUbicacionActual() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val coordenadas = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                    binding.addressEditText.setText(coordenadas)
                    Toast.makeText(this, "Ubicación obtenida exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación. Asegúrate de tener el GPS activo.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun procesarCompra() {
        val name = binding.nameEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        val notes = binding.notesEditText.text.toString()

        if (name.isBlank() || address.isBlank() || phone.isBlank()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val detalleServicios = cartItems.joinToString(separator = "\n") { "- ${it.nombreServicio} ($${it.precio})" }
        val descripcionFinal = "Cliente: $name\nDirección: $address\nTel: $phone\nNotas: $notes\n\nServicios:\n$detalleServicios"

        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val fotoUriParaSolicitud: String? = if (cartItems.isNotEmpty()) {
            cartItems.firstOrNull()?.fotoUri
        } else {
            null
        }

        val sharedPrefs = getSharedPreferences("BiciReparoPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPrefs.getString("USER_EMAIL", "invitado@bicireparo.com") ?: "invitado@bicireparo.com"

        val nuevaSolicitud = Solicitud(
            descripcion = descripcionFinal,
            fecha = fechaActual,
            fotoUri = fotoUriParaSolicitud,
            tipoServicio = "Compra Online",
            precioEstimado = totalAmount,
            usuarioEmail = userEmail
        )

        viewModel.insert(nuevaSolicitud)
        viewModel.comprarYVaciar()

        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra("EXTRA_TOTAL", totalAmount)
        intent.putExtra("EXTRA_FECHA", fechaActual)
        intent.putExtra("EXTRA_DETALLE", descripcionFinal)

        startActivity(intent)
        finish()
    }
}