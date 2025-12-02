package com.example.bicireparoapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bicireparoapp.databinding.ActivityReceiptBinding

class ReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val total = intent.getIntExtra("EXTRA_TOTAL", 0)
        val fecha = intent.getStringExtra("EXTRA_FECHA") ?: ""
        val detalle = intent.getStringExtra("EXTRA_DETALLE") ?: ""

        binding.receiptTotalText.text = "$ $total"
        binding.receiptDateText.text = "Fecha: $fecha"
        binding.receiptDetailText.text = detalle

        binding.homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}