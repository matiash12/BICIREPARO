package com.example.bicireparoapp.utils

import com.example.bicireparoapp.model.Carrito

object CalculadoraUtils {

    /**
     * Función que calcula el total de una lista de productos del carrito.
     * Si el total supera los $50.000, aplica un descuento del 10%.
     * Esta es la lógica que vamos a probar.
     */
    fun calcularTotalConDescuento(items: List<Carrito>): Int {
        var total = 0
        for (item in items) {
            total += item.precio
        }

        // Regla de negocio: Si compra más de 50.000, descuento del 10%
        if (total > 50000) {
            total = (total * 0.90).toInt()
        }

        return total
    }
}