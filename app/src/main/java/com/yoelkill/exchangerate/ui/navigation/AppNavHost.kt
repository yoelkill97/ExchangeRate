package com.yoelkill.exchangerate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yoelkill.exchangerate.ui.screen.ConversionHistoryScreen
import com.yoelkill.exchangerate.ui.screen.ExchangeRateCalculatorScreen
import com.yoelkill.exchangerate.ui.screen.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route,
        modifier = modifier
    ) {
        composable(route = Routes.SplashScreen.route) {
            SplashScreen(onFinished = {
                navController.navigate(route =
                    Routes.ExchangeRateCalculatorScreen.route) {
                    popUpTo(route =
                        Routes.SplashScreen.route) {
                        inclusive = true
                    }
                }
            })
        }
        composable(route = Routes.ExchangeRateCalculatorScreen.route) {
            ExchangeRateCalculatorScreen(navigateConversion = {
                navController.navigate(route = Routes.ConversionHistoryScreen.route)
            })
        }
        composable(route = Routes.ConversionHistoryScreen.route) {
            ConversionHistoryScreen()
        }

    }
}