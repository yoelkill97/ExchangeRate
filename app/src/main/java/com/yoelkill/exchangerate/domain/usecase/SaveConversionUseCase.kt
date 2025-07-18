package com.yoelkill.exchangerate.domain.usecase


import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.domain.repository.ExchangeRepository
import javax.inject.Inject

class SaveConversionUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(conversion: ConversionHistory):Result<Unit> {
      return  repository.saveConversion(conversion)
    }
}
