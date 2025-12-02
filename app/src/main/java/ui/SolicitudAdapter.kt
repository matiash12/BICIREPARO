package com.example.bicireparoapp.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bicireparoapp.R
import com.example.bicireparoapp.databinding.ItemSolicitudBinding
import com.example.bicireparoapp.model.Solicitud

class SolicitudAdapter(
    private var solicitudes: List<Solicitud>,
    private val onDeleteClick: (Solicitud) -> Unit
) : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>() {

    inner class SolicitudViewHolder(private val binding: ItemSolicitudBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(solicitud: Solicitud) {
            binding.solicitudDescriptionTextView.text = solicitud.descripcion
            binding.solicitudDateTextView.text = solicitud.fecha

            // --- NUEVO: Cargar la imagen de la solicitud ---
            if (solicitud.fotoUri != null) {
                try {
                    binding.solicitudImageView.setImageURI(Uri.parse(solicitud.fotoUri))
                } catch (e: Exception) {
                    binding.solicitudImageView.setImageResource(R.drawable.ic_popular)
                }
            } else {
                binding.solicitudImageView.setImageResource(R.drawable.ic_popular)
            }
            // ------------------------------------------------

            binding.solicitudDeleteButton.setOnClickListener {
                onDeleteClick(solicitud)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val binding = ItemSolicitudBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SolicitudViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        holder.bind(solicitudes[position])
    }

    override fun getItemCount() = solicitudes.size

    fun actualizarLista(nuevaLista: List<Solicitud>) {
        solicitudes = nuevaLista
        notifyDataSetChanged()
    }
}