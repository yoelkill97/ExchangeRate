package com.yoelkill.exchangerate.domain.usecase


import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConversionHistoryUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    operator fun invoke(): Flow<List<ConversionHistory>> {
        return repository.getConversionHistory()
    }
}
