package com.yoelkill.exchangerate.data.local.mapper

import com.yoelkill.exchangerate.data.remote.dao.ExchangeRateResponse
import com.yoelkill.exchangerate.domain.model.ExchangeRate

interface ExchangeRateMapper{
    suspend fun mapToDomain(exchangeRateResponse: ExchangeRateResponse): ExchangeRate
}