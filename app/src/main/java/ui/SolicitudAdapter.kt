package com.example.bicireparoapp.ui // O "com.example.bicireparoapp.adapter" si creaste ese paquete

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.bicireparoapp.databinding.ItemSolicitudBinding // Importar el ViewBinding del item
import com.example.bicireparoapp.model.Solicitud // Importar el modelo

/**
 * Este Adapter conecta la lista de 'Solicitud' con el RecyclerView.
 * Recibe una función lambda 'onDeleteClicked' que será llamada cuando
 * el usuario presione el botón de eliminar.
 */
class SolicitudAdapter(
    private val onDeleteClicked: (Solicitud) -> Unit
) : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>() {

    // Esta es la lista de solicitudes que el adapter mostrará.
    private var solicitudes: List<Solicitud> = emptyList()

    /**
     * 'ViewHolder' es una clase que representa CADA fila de la lista.
     * Guarda las referencias a los views (TextViews, ImageView)
     * para no tener que buscarlos cada vez.
     */
    inner class SolicitudViewHolder(private val binding: ItemSolicitudBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(solicitud: Solicitud) {
            // 1. Poner los datos en los Views
            binding.itemDescripcionTextView.text = solicitud.descripcion
            binding.itemFechaTextView.text = solicitud.fecha

            // 2. Cargar la foto
            try {
                val fotoUri = solicitud.fotoUri?.toUri()
                binding.itemFotoImageView.setImageURI(fotoUri)
            } catch (e: Exception) {
                // En caso de error (ej. URI nula o inválida), poner una imagen por defecto
                binding.itemFotoImageView.setImageResource(android.R.drawable.ic_menu_camera)
            }

            // 3. Configurar el botón de eliminar
            binding.itemDeleteButton.setOnClickListener {
                // Llama a la función lambda que nos pasaron, enviando
                // la solicitud que debe ser eliminada.
                onDeleteClicked(solicitud)
            }
        }
    }

    /**
     * Se llama cuando el RecyclerView necesita crear una NUEVA fila (ViewHolder).
     * Infla el layout XML 'item_solicitud.xml'.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val binding = ItemSolicitudBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SolicitudViewHolder(binding)
    }

    /**
     * Se llama para MOSTRAR los datos en una fila específica (posición).
     * Toma el dato de la lista y llama a la función 'bind' del ViewHolder.
     */
    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        holder.bind(solicitudes[position])
    }

    /**
     * Le dice al RecyclerView cuántos ítems hay en total en la lista.
     */
    override fun getItemCount(): Int {
        return solicitudes.size
    }

    /**
     * Función pública para actualizar la lista de solicitudes en el adapter.
     * Notifica al RecyclerView que los datos han cambiado.
     */
    fun submitList(nuevaLista: List<Solicitud>) {
        solicitudes = nuevaLista
        notifyDataSetChanged() // Refresca la lista completa
    }
}