package com.yoelkill.exchangerate.domain.usecase

import com.yoelkill.exchangerate.domain.util.Resource
import com.yoelkill.exchangerate.domain.model.ExchangeRate
import com.yoelkill.exchangerate.domain.repository.ExchangeRepository
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(): Resource<ExchangeRate> {
        return repository.getExchangeRate()
    }
}
