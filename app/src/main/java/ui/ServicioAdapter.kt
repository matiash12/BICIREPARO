package com.example.bicireparoapp.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bicireparoapp.R // ¡Importante para los recursos!
import com.example.bicireparoapp.databinding.ItemServicioBinding
import com.example.bicireparoapp.model.Servicio

class ServicioAdapter(
    private var servicios: List<Servicio>,
    private val onItemClick: (Servicio) -> Unit
) : RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder>() {

    inner class ServicioViewHolder(private val binding: ItemServicioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(servicio: Servicio) {
            // Asignamos textos
            binding.itemNombreTextView.text = servicio.nombre
            binding.itemDescripcionTextView.text = servicio.descripcion
            binding.itemPrecioTextView.text = "$ ${servicio.precio}"

            // --- Cargar la imagen con seguridad ---
            if (servicio.fotoUri != null) {
                try {
                    // Intentamos cargar la URI de la foto guardada
                    binding.itemServiceImage.setImageURI(Uri.parse(servicio.fotoUri))
                } catch (e: Exception) {
                    // Si falla (ej. archivo borrado), ponemos la imagen por defecto
                    binding.itemServiceImage.setImageResource(R.drawable.ic_popular)
                }
            } else {
                // Si no tiene foto, ponemos la de defecto
                binding.itemServiceImage.setImageResource(R.drawable.ic_popular)
            }
            // --------------------------------------

            // Configurar el clic en la tarjeta
            binding.root.setOnClickListener {
                onItemClick(servicio)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val binding = ItemServicioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        holder.bind(servicios[position])
    }

    override fun getItemCount() = servicios.size

    // Método para actualizar la lista desde la Activity
    fun actualizarLista(nuevaLista: List<Servicio>) {
        servicios = nuevaLista
        notifyDataSetChanged()
    }
}