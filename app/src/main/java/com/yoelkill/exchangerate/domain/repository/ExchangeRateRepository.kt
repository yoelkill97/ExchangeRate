package com.yoelkill.exchangerate.domain.repository


import com.yoelkill.exchangerate.domain.util.Resource
import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.domain.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {


    suspend fun getExchangeRate(): Resource<ExchangeRate>


    suspend fun saveConversion(conversion: ConversionHistory) : Result<Unit>

    fun getConversionHistory(): Flow<List<ConversionHistory>>


    suspend fun clearConversionHistory(): Result<Unit>
}
