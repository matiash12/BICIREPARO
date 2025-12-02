package com.example.bicireparoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL para la API del Dólar (Internet)
    private const val CURRENCY_URL = "https://mindicador.cl/"

    // URLs para tus Microservicios (Localhost desde el emulador es 10.0.2.2)
    private const val USER_URL = "http://192.168.1.17:8080/"  // Puerto 8080 (Usuarios)
    private const val BICI_URL = "http://192.168.1.17:8081/"  // Puerto 8081 (Servicios)

    // 1. Cliente para el Dólar
    val currencyInstance: CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl(CURRENCY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }

    // 2. Cliente para Usuarios (User Service)
    val userInstance: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(USER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    // 3. Cliente para Servicios (Bici Service)
    val biciInstance: BiciApi by lazy {
        Retrofit.Builder()
            .baseUrl(BICI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BiciApi::class.java)
    }
}