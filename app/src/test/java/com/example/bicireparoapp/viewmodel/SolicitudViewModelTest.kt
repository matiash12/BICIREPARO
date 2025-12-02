package com.example.bicireparoapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bicireparoapp.data.BiciRepository
import com.example.bicireparoapp.model.Carrito
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor // Necesitas mockito-kotlin para esto

@ExperimentalCoroutinesApi
class SolicitudViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: BiciRepository

    private lateinit var viewModel: SolicitudViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SolicitudViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agregarAlCarrito llama al repositorio con el item correcto`() = runTest {
        // DADO
        val nombre = "Servicio Test"
        val precio = 5000
        val foto = null

        // CUANDO
        viewModel.agregarAlCarrito(nombre, precio, foto)
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES (Verificamos que se llamó a repository.agregarAlCarrito)
        // Usamos un "captor" para ver qué objeto se envió
        val captor = argumentCaptor<Carrito>()
        Mockito.verify(repository).agregarAlCarrito(captor.capture())

        val itemCapturado = captor.firstValue
        assert(itemCapturado.nombreServicio == nombre)
        assert(itemCapturado.precio == precio)
    }

    @Test
    fun `comprarYVaciar llama a vaciar el carrito`() = runTest {
        // CUANDO
        viewModel.comprarYVaciar()
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES
        Mockito.verify(repository).vaciarCarrito()
    }
}