package com.yoelkill.exchangerate.domain.usecase

import com.yoelkill.exchangerate.domain.repository.ExchangeRepository
import javax.inject.Inject

class CleanConversionHistoryUseCase  @Inject constructor(
    private val repository: ExchangeRepository
){

    suspend fun invoke(): Result<Unit> {
        return repository.clearConversionHistory()
    }

}