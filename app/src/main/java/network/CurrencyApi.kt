package com.example.bicireparoapp.network

import com.example.bicireparoapp.model.IndicadoresResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("api") // Endpoint de mindicador.cl
    suspend fun getIndicadores(): Response<IndicadoresResponse>
}