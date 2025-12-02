package com.example.bicireparoapp.network

import com.example.bicireparoapp.model.ServicioBackend
import com.example.bicireparoapp.model.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path

// API para el puerto 8080 (Usuarios)
interface UserApi {
    // --- Login y Registro (Ya los tenías) ---
    @POST("api/users/login")
    suspend fun login(@Body credenciales: Map<String, String>): Response<UsuarioResponse>

    @POST("api/users/register")
    suspend fun registrar(@Body usuario: Map<String, String>): Response<UsuarioResponse>

    // --- NUEVO: Listar todos los usuarios (GET) ---
    @GET("api/users")
    suspend fun getUsuarios(): Response<List<UsuarioResponse>>

    // --- NUEVO: Actualizar un usuario (PUT) ---
    // Usamos @Path("id") para poner el número en la URL: api/users/5
    @PUT("api/users/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body datos: Map<String, String> // Enviamos nombre, email, password, rol
    ): Response<UsuarioResponse>

    // --- NUEVO: Eliminar un usuario (DELETE) ---
    @DELETE("api/users/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long): Response<String>
}

// API para el puerto 8081 (Servicios)
interface BiciApi {
    @GET("api/servicios")
    suspend fun getServicios(): Response<List<ServicioBackend>>
}