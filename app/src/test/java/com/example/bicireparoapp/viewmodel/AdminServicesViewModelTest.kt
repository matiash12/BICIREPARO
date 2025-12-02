package com.example.bicireparoapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.bicireparoapp.data.BiciRepository
import com.example.bicireparoapp.model.Servicio
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

@ExperimentalCoroutinesApi
class AdminServicesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: BiciRepository

    @Mock
    private lateinit var servicesObserver: Observer<List<Servicio>>

    private lateinit var viewModel: AdminServicesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AdminServicesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert servicio llama al repositorio`() = runTest {
        // DADO
        val servicio = Servicio(nombre = "Lavado", descripcion = "Lavado simple", precio = 5000)

        // CUANDO
        viewModel.insert(servicio)
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES
        Mockito.verify(repository).insertServicio(servicio)
    }

    @Test
    fun `delete servicio llama al repositorio`() = runTest {
        // DADO
        val servicio = Servicio(id = 1, nombre = "Borrar", descripcion = "x", precio = 0)

        // CUANDO
        viewModel.delete(servicio)
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES//
        Mockito.verify(repository).deleteServicio(servicio)
    }

    @Test
    fun `update servicio llama al repositorio`() = runTest {
        // DADO
        val servicio = Servicio(id = 2, nombre = "Actualizar", descripcion = "y", precio = 100)

        // CUANDO
        viewModel.update(servicio)
        testDispatcher.scheduler.advanceUntilIdle()

        // ENTONCES
        Mockito.verify(repository).updateServicio(servicio)
    }
}