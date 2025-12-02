package com.example.bicireparoapp

import com.example.bicireparoapp.model.Carrito
import com.example.bicireparoapp.utils.CalculadoraUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * PRUEBA UNITARIA (Requisito IE 3.2.2)
 * Objetivo: Validar que la lógica de cálculo de precios y descuentos funcione correctamente
 * sin necesidad de ejecutar la app en el celular.
 */
class CalculadoraUtilsTest {

    @Test
    fun `calcularTotal_sumaCorrecta_sinDescuento`() {
        // 1. DADO (Given): Una lista de productos que suman 20.000 (menos de 50k)
        val items = listOf(
            Carrito(nombreServicio = "Servicio 1", precio = 10000),
            Carrito(nombreServicio = "Servicio 2", precio = 10000)
        )

        // 2. CUANDO (When): Calculamos el total
        val resultado = CalculadoraUtils.calcularTotalConDescuento(items)

        // 3. ENTONCES (Then): El resultado debe ser 20.000 exactos
        assertEquals(20000, resultado)
    }

    @Test
    fun `calcularTotal_aplicaDescuento_mayorA50k`() {
        // 1. DADO: Una lista de productos que suman 100.000 (más de 50k)
        val items = listOf(
            Carrito(nombreServicio = "Servicio Caro", precio = 100000)
        )

        // 2. CUANDO: Calculamos el total
        val resultado = CalculadoraUtils.calcularTotalConDescuento(items)

        // 3. ENTONCES: Debería aplicar 10% de descuento.
        // 100.000 - 10% = 90.000
        assertEquals(90000, resultado)
    }


}