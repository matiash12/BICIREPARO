package com.example.bicireparoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bicireparoapp.databinding.ItemCarritoBinding
import com.example.bicireparoapp.model.Carrito

class CartAdapter(
    private val onDeleteClick: (Carrito) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items: List<Carrito> = emptyList()

    inner class CartViewHolder(private val binding: ItemCarritoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Carrito) {
            binding.cartItemName.text = item.nombreServicio
            binding.cartItemPrice.text = "$ ${item.precio}"
            binding.cartItemDelete.setOnClickListener { onDeleteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<Carrito>) {
        items = newItems
        notifyDataSetChanged()
    }
}