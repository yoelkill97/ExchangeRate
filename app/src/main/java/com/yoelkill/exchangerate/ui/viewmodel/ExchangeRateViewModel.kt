package com.yoelkill.exchangerate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.domain.model.ExchangeRate
import com.yoelkill.exchangerate.domain.usecase.GetExchangeRateUseCase
import com.yoelkill.exchangerate.domain.usecase.SaveConversionUseCase
import com.yoelkill.exchangerate.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val saveConversionUseCase: SaveConversionUseCase
) : ViewModel() {
    private val _exchangeRateState =
        MutableStateFlow<ExchangeRateUiState>(ExchangeRateUiState.Loading)
    val exchangeRateState: StateFlow<ExchangeRateUiState> = _exchangeRateState.asStateFlow()

    private val _buyRate = MutableStateFlow("")
    val buyRate: StateFlow<String> = _buyRate

    private val _sellRate = MutableStateFlow("")
    val sellRate: StateFlow<String> = _sellRate

    private val _amountToConvert = MutableStateFlow("")
    val amountToConvert: StateFlow<String> = _amountToConvert

    private val _convertedAmount = MutableStateFlow("")
    val convertedAmount: StateFlow<String> = _convertedAmount

    private val _sourceCurrency = MutableStateFlow("USD")
    val sourceCurrency: StateFlow<String> = _sourceCurrency

    private val _targetCurrency = MutableStateFlow("PEN")
    val targetCurrency: StateFlow<String> = _targetCurrency


    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        fetchExchangeRate()
    }

    fun updateAmountToConvert(newAmount: String) {
        _amountToConvert.value = newAmount
        convertCurrency()
    }

    fun swapCurrencies() {
        val currentSource = _sourceCurrency.value
        _sourceCurrency.value = _targetCurrency.value
        _targetCurrency.value = currentSource
        convertCurrency()
    }

    fun fetchExchangeRate() {
        viewModelScope.launch {
            _exchangeRateState.value = ExchangeRateUiState.Loading
            delay(2000)
            when (val result =
                getExchangeRateUseCase()) {
                is Resource.Success -> {
                    val rateData = result.data
                    if (rateData != null) {
                        _exchangeRateState.value = ExchangeRateUiState.Success(rateData)
                        _buyRate.value = rateData.purchaseRate.toString()
                        _sellRate.value = rateData.sellRate.toString()
                    } else {
                        _exchangeRateState.value = ExchangeRateUiState.Error("Data is null")
                        _buyRate.value = ""
                        _sellRate.value = ""
                        _convertedAmount.value = ""
                    }
                }

                is Resource.Error -> {
                    _exchangeRateState.value =
                        ExchangeRateUiState.Error(result.message ?: "Un error inesperado ocurrio")
                }

                else -> {}
            }
        }
    }


    private fun convertCurrency() {
        val amount = _amountToConvert.value.toDoubleOrNull()

        val rateToUse =
            if (_sourceCurrency.value == "USD" && _targetCurrency.value == "PEN") {
                _sellRate.value.toDoubleOrNull()
            } else if (_sourceCurrency.value == "PEN" && _targetCurrency.value == "USD") {
                _buyRate.value.toDoubleOrNull()?.let { if (it > 0) 1 / it else null }
            } else {
                _sellRate.value.toDoubleOrNull()
            }


        if (amount != null && amount > 0 && rateToUse != null && rateToUse > 0) {
            _convertedAmount.value = String.format("%.2f", amount * rateToUse)
        } else {
            _convertedAmount.value = ""
        }
    }

    fun saveConversion() {
        val amountToConvertValue = _amountToConvert.value.toDoubleOrNull() ?: 0.0
        val convertedAmountValue = _convertedAmount.value.toDoubleOrNull() ?: 0.0

        if (amountToConvertValue <= 0 || convertedAmountValue <= 0) {
            _userMessage.value = "Ingrese una cantidad válida para convertir."
            return
        }

        viewModelScope.launch {
            val currentTimestampMillis = System.currentTimeMillis()
            val conversion = ConversionHistory(
                fromCurrency = _sourceCurrency.value,
                toCurrency = _targetCurrency.value,
                fromAmount = amountToConvertValue,
                toAmount = convertedAmountValue,
                timestamp = currentTimestampMillis
            )

            val result = saveConversionUseCase(conversion)
            result.fold(
                onSuccess = {
                    _userMessage.value = "Se guardó la conversión exitosamente!"

                },
                onFailure = { exception ->
                    _userMessage.value = "No se pudo guardar la conversión: ${exception.localizedMessage}"
                }
            )
        }
    }
    fun userMessageShown() {
        _userMessage.value = null
    }
}

sealed class ExchangeRateUiState {
    data class Success(val exchangeRate: ExchangeRate) : ExchangeRateUiState()
    data class Error(val message: String) : ExchangeRateUiState()
    object Loading : ExchangeRateUiState()
}