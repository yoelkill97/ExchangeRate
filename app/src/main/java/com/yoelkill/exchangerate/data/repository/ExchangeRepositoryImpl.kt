package com.yoelkill.exchangerate.data.repository

import android.util.Log
import com.yoelkill.exchangerate.data.local.dao.ConversionHistoryDao
import com.yoelkill.exchangerate.data.local.mapper.ConversionHistoryMapper
import com.yoelkill.exchangerate.data.remote.api.ExchangeApiService
import com.yoelkill.exchangerate.data.local.mapper.ExchangeRateMapper
import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.domain.model.ExchangeRate
import com.yoelkill.exchangerate.domain.repository.ExchangeRepository
import com.yoelkill.exchangerate.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val apiService: ExchangeApiService,
    private val historyDao: ConversionHistoryDao,
    private val exchangeRateMapper: ExchangeRateMapper,
    private val conversionHistoryMapper: ConversionHistoryMapper
) : ExchangeRepository {

    override suspend fun getExchangeRate(): Resource<ExchangeRate> {
        return try {
            val response = apiService.getLatestRates()
            val domainRates = exchangeRateMapper.mapToDomain(response)
            Resource.Success(domainRates)
        } catch (e: IOException) {
            Resource.Error("Network error: ${e.localizedMessage}")
        } catch (e: HttpException) {
            Resource.Error("API error: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun saveConversion(conversion: ConversionHistory) : Result<Unit>{
        //historyDao.insert(conversionHistoryMapper.mapToEntity(conversion))
        return try {
            val entity = conversionHistoryMapper.mapToEntity(conversion)
            historyDao.insert(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getConversionHistory(): Flow<List<ConversionHistory>> {
        return historyDao.getHistory().map { entities ->
            entities.map { conversionHistoryMapper.mapToDomain(it)}
        }
    }

    override suspend fun clearConversionHistory() : Result<Unit>{
        return try {
            historyDao.clearHistory()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}