package com.yoelkill.exchangerate.domain.model

data class ConversionHistory(
    val id: Int = 0,
    val fromCurrency: String,
    val toCurrency: String,
    val fromAmount: Double,
    val toAmount: Double,
    val timestamp: Long
)
