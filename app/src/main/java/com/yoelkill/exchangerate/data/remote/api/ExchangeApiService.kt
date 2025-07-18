package com.yoelkill.exchangerate.data.remote.api

import com.yoelkill.exchangerate.data.remote.dao.ExchangeRateResponse
import retrofit2.http.GET

interface ExchangeApiService {
    @GET("exchange")
    suspend fun getLatestRates(): ExchangeRateResponse
}