package com.example.bicireparoapp.data

import androidx.lifecycle.LiveData
import com.example.bicireparoapp.model.Carrito
import com.example.bicireparoapp.model.Servicio // Importar
import com.example.bicireparoapp.model.Solicitud
import com.example.bicireparoapp.model.Usuario

class BiciRepository(
    private val solicitudDao: SolicitudDao,
    private val carritoDao: CarritoDao,
    private val usuarioDao: UsuarioDao,
    private val servicioDao: ServicioDao // <--- Inyectamos el nuevo DAO
) {

    // --- Usuarios ---
    val allUsuarios: LiveData<List<Usuario>> = usuarioDao.getAllUsuarios() // Nuevo

    suspend fun insertUsuario(usuario: Usuario) = usuarioDao.insert(usuario)
    suspend fun updateUsuario(usuario: Usuario) = usuarioDao.update(usuario) // Nuevo
    suspend fun deleteUsuario(usuario: Usuario) = usuarioDao.delete(usuario) // Nuevo
    suspend fun buscarUsuarioPorEmail(email: String) = usuarioDao.getUsuarioByEmail(email)

    // --- SERVICIOS (CRUD) ---
    // Esta lista se actualizará sola cuando cambie la base de datos
    val allServicios: LiveData<List<Servicio>> = servicioDao.obtenerTodos()

    suspend fun insertServicio(servicio: Servicio) {
        servicioDao.insertar(servicio)
    }

    suspend fun updateServicio(servicio: Servicio) {
        servicioDao.actualizar(servicio)
    }

    suspend fun deleteServicio(servicio: Servicio) {
        servicioDao.eliminar(servicio)
    }

    // --- SOLICITUDES ---
    // ¡NUEVO MÉTODO!
    val allSolicitudes: LiveData<List<Solicitud>> = solicitudDao.getAllSolicitudes()

    fun getSolicitudesPorUsuario(email: String): LiveData<List<Solicitud>> {
        return solicitudDao.getSolicitudesPorUsuario(email)
    }

    suspend fun insertSolicitud(solicitud: Solicitud) = solicitudDao.insert(solicitud)
    suspend fun deleteSolicitud(solicitud: Solicitud) = solicitudDao.delete(solicitud)

    // --- CARRITO ---
    val itemsCarrito: LiveData<List<Carrito>> = carritoDao.obtenerCarrito()
    val totalCarrito: LiveData<Int> = carritoDao.obtenerTotal()

    suspend fun agregarAlCarrito(item: Carrito) {
        carritoDao.insertar(item)
    }

    suspend fun eliminarDelCarrito(item: Carrito) {
        carritoDao.eliminar(item)
    }

    suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }
}