package com.example.bicireparoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bicireparoapp.databinding.ItemUsuarioBinding
import com.example.bicireparoapp.model.UsuarioResponse // <--- CAMBIO IMPORTANTE

class UserAdapter(
    // Ahora la lista es de UsuarioResponse (lo que viene del servidor)
    private var usuarios: List<UsuarioResponse>,
    private val onEditClick: (UsuarioResponse) -> Unit,
    private val onDeleteClick: (UsuarioResponse) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: UsuarioResponse) {
            binding.userNameText.text = usuario.nombre
            binding.userEmailText.text = usuario.email
            binding.userRoleText.text = "ROL: ${usuario.rol.uppercase()}"

            // Los botones ahora pasan el objeto UsuarioResponse completo
            binding.btnEdit.setOnClickListener { onEditClick(usuario) }
            binding.btnDelete.setOnClickListener { onDeleteClick(usuario) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(usuarios[position])
    }

    override fun getItemCount() = usuarios.size

    fun actualizarLista(nuevaLista: List<UsuarioResponse>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }
}