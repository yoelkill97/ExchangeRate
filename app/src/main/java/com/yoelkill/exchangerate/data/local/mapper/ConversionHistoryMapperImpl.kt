package com.yoelkill.exchangerate.data.local.mapper

import com.yoelkill.exchangerate.data.local.entity.ConversionHistoryEntity
import com.yoelkill.exchangerate.domain.model.ConversionHistory
import jakarta.inject.Inject

class ConversionHistoryMapperImpl @Inject constructor() : ConversionHistoryMapper {
    override fun mapToDomain(entity: ConversionHistoryEntity): ConversionHistory {
        return ConversionHistory(
            id = entity.id,
            fromCurrency = entity.fromCurrency,
            fromAmount = entity.fromAmount,
            toCurrency = entity.toCurrency,
            toAmount = entity.toAmount,
            timestamp = entity.timestamp
        )
    }

    override fun mapToEntity(domain: ConversionHistory): ConversionHistoryEntity {
        return ConversionHistoryEntity(
            id = domain.id,
            fromCurrency = domain.fromCurrency,
            fromAmount = domain.fromAmount,
            toCurrency = domain.toCurrency,
            toAmount = domain.toAmount,
            timestamp = domain.timestamp
        )
    }
}