package com.yoelkill.exchangerate.data.local.mapper

import com.yoelkill.exchangerate.data.local.entity.ConversionHistoryEntity
import com.yoelkill.exchangerate.domain.model.ConversionHistory

interface ConversionHistoryMapper {
    fun mapToDomain(entity: ConversionHistoryEntity): ConversionHistory
    fun mapToEntity(domain: ConversionHistory): ConversionHistoryEntity
}