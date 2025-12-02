package com.example.bicireparoapp.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.bicireparoapp.model.UsuarioResponse
import com.example.bicireparoapp.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.* // Importamos Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: UserApi

    @Mock
    private lateinit var application: Application

    // Observadores (Los necesitamos para que el LiveData esté activo, pero no los verificaremos con Mockito)
    @Mock
    private lateinit var loginResultObserver: Observer<Result<UsuarioResponse>>
    @Mock
    private lateinit var registerResultObserver: Observer<Result<UsuarioResponse>>

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = LoginViewModel(application, api)

        // Conectamos los observadores para activar el LiveData
        viewModel.loginResult.observeForever(loginResultObserver)
        viewModel.registerResult.observeForever(registerResultObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.loginResult.removeObserver(loginResultObserver)
        viewModel.registerResult.removeObserver(registerResultObserver)
    }

    // --- TEST 1: LOGIN EXITOSO ---
    @Test
    fun `login con credenciales correctas devuelve SUCCESS`() = runTest {
        // DADO
        val usuarioExitoso = UsuarioResponse(1, "Test User", "test@mail.com", "cliente")
        val respuestaExitosa = Response.success(usuarioExitoso)

        // Usamos nuestro helper 'anyObject()' para evitar el NullPointerException de Kotlin
        Mockito.`when`(api.login(anyObject())).thenReturn(respuestaExitosa)

        // CUANDO
        viewModel.login("test@mail.com", "123")
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES (Verificamos el valor directamente)
        val resultado = viewModel.loginResult.value
        assertNotNull(resultado)
        assertTrue(resultado!!.isSuccess)
        assertEquals(usuarioExitoso, resultado.getOrNull())
    }

    // --- TEST 2: LOGIN FALLIDO ---
    @Test
    fun `login con credenciales incorrectas devuelve FAILURE`() = runTest {
        // DADO
        val errorBody = "Error".toResponseBody("text/plain".toMediaTypeOrNull())
        val respuestaError = Response.error<UsuarioResponse>(401, errorBody)

        Mockito.`when`(api.login(anyObject())).thenReturn(respuestaError)

        // CUANDO
        viewModel.login("error@mail.com", "badpass")
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES
        val resultado = viewModel.loginResult.value
        assertNotNull(resultado)
        assertTrue(resultado!!.isFailure)
        assertEquals("Credenciales incorrectas", resultado.exceptionOrNull()?.message)
    }

    // --- TEST 3: REGISTRO EXITOSO ---
    @Test
    fun `registrar usuario nuevo devuelve SUCCESS`() = runTest {
        // DADO
        val nuevoUsuario = UsuarioResponse(2, "Nuevo", "nuevo@mail.com", "cliente")
        val respuesta = Response.success(nuevoUsuario)

        Mockito.`when`(api.registrar(anyObject())).thenReturn(respuesta)

        // CUANDO
        viewModel.registrar("Nuevo", "nuevo@mail.com", "123", "cliente")
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES
        val resultado = viewModel.registerResult.value
        assertNotNull(resultado)
        assertTrue(resultado!!.isSuccess)
        assertEquals(nuevoUsuario, resultado.getOrNull())
    }

    // --- TEST 4: REGISTRO FALLIDO ---
    @Test
    fun `registrar usuario existente devuelve FAILURE`() = runTest {
        // DADO
        val errorBody = "Existe".toResponseBody("text/plain".toMediaTypeOrNull())
        val respuestaError = Response.error<UsuarioResponse>(400, errorBody)

        Mockito.`when`(api.registrar(anyObject())).thenReturn(respuestaError)

        // CUANDO
        viewModel.registrar("Duplicado", "existe@mail.com", "123", "cliente")
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES
        val resultado = viewModel.registerResult.value
        assertNotNull(resultado)
        assertTrue(resultado!!.isFailure)
        assertEquals("Error al registrar", resultado.exceptionOrNull()?.message)
    }

    // --- HELPER MÁGICO PARA KOTLIN ---
    // Esto engaña a Kotlin para que acepte el null de Mockito sin quejarse
    private fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}