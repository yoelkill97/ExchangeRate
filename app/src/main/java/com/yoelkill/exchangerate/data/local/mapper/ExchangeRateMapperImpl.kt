package com.yoelkill.exchangerate.data.local.mapper

import com.yoelkill.exchangerate.data.remote.dao.ExchangeRateResponse
import com.yoelkill.exchangerate.domain.model.ExchangeRate
import javax.inject.Inject

class ExchangeRateMapperImpl @Inject constructor() : ExchangeRateMapper {
    override suspend fun mapToDomain(exchangeRateResponse: ExchangeRateResponse): ExchangeRate {
       return  ExchangeRate(
            purchaseRate = exchangeRateResponse.purchaseRate,
            sellRate = exchangeRateResponse.saleRate
        )
    }
}