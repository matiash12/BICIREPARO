package com.example.bicireparoapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bicireparoapp.BiciReparoApplication
import com.example.bicireparoapp.databinding.ActivityCartBinding
import com.example.bicireparoapp.viewmodel.SolicitudViewModel
import com.example.bicireparoapp.viewmodel.SolicitudViewModelFactory

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    private val viewModel: SolicitudViewModel by viewModels {
        SolicitudViewModelFactory((application as BiciReparoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón Checkout
        binding.checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }

        // Botón Volver (si existe en tu XML, si no bórralo)


        // Configurar RecyclerView
        cartAdapter = CartAdapter { itemToDelete ->
            viewModel.eliminarDelCarrito(itemToDelete)
        }
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = cartAdapter

        // Observar items
        viewModel.itemsCarrito.observe(this, Observer { items ->
            if (items.isEmpty()) {
                binding.emptyCartMsg.visibility = View.VISIBLE
                binding.cartRecyclerView.visibility = View.GONE
                binding.checkoutButton.isEnabled = false
            } else {
                binding.emptyCartMsg.visibility = View.GONE
                binding.cartRecyclerView.visibility = View.VISIBLE
                binding.checkoutButton.isEnabled = true
                cartAdapter.submitList(items)
            }
        })

        // Observar total
        viewModel.totalCarrito.observe(this, Observer { total ->
            val monto = total ?: 0
            binding.totalPriceText.text = "$ $monto"
        })
    }
}