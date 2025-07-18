package com.yoelkill.exchangerate.ui.navigation

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splash")
    object ExchangeRateCalculatorScreen : Routes("exchangeRateCalculator")
    object ConversionHistoryScreen : Routes("conversionHistory")
}