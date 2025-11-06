package com.example.bicireparoapp.ui // O "com.example.bicireparoapp.adapter"

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bicireparoapp.databinding.ItemServicioBinding // Importar el ViewBinding del item
import com.example.bicireparoapp.model.Servicio // Importar el modelo

/**
 * Este Adapter conecta la lista de 'Servicio' con el RecyclerView.
 * Es más simple que el otro adapter porque no tiene botón de eliminar.
 */
class ServicioAdapter(
    private val servicios: List<Servicio>
) : RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder>() {

    /**
     * ViewHolder que representa CADA fila de servicio.
     */
    inner class ServicioViewHolder(private val binding: ItemServicioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(servicio: Servicio) {
            // Poner los datos en los Views
            binding.itemNombreTextView.text = servicio.nombre
            binding.itemDescripcionTextView.text = servicio.descripcion
            binding.itemPrecioTextView.text = servicio.precio
        }
    }

    /**
     * Se llama para crear una NUEVA fila.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val binding = ItemServicioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServicioViewHolder(binding)
    }

    /**
     * Se llama para MOSTRAR los datos en una fila específica.
     */
    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        holder.bind(servicios[position])
    }

    /**
     * Le dice al RecyclerView cuántos ítems hay en total.
     */
    override fun getItemCount(): Int {
        return servicios.size
    }
}