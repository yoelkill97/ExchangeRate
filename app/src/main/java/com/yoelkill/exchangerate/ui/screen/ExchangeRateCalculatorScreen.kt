package com.yoelkill.exchangerate.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yoelkill.exchangerate.R
import com.yoelkill.exchangerate.ui.theme.ExchangeRateTheme
import com.yoelkill.exchangerate.ui.viewmodel.ExchangeRateUiState
import com.yoelkill.exchangerate.ui.viewmodel.ExchangeRateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateCalculatorScreen(exchangeRateViewModel: ExchangeRateViewModel = hiltViewModel(),navigateConversion : () -> Unit = {} ) {
    val exchangeRateUiState by exchangeRateViewModel.exchangeRateState.collectAsState()
    val buyRate by exchangeRateViewModel.buyRate.collectAsState()
    val sellRate by exchangeRateViewModel.sellRate.collectAsState()
    val amountToConvert by exchangeRateViewModel.amountToConvert.collectAsState()
    val convertedAmount by exchangeRateViewModel.convertedAmount.collectAsState()
    val sourceCurrency by exchangeRateViewModel.sourceCurrency.collectAsState()
    val targetCurrency by exchangeRateViewModel.targetCurrency.collectAsState()
    val userMessage by exchangeRateViewModel.userMessage.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            exchangeRateViewModel.userMessageShown()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (exchangeRateUiState) {
                is ExchangeRateUiState.Loading -> {
                    CircularProgressIndicator()
                    Text("Loading exchange rates...")
                }

                is ExchangeRateUiState.Success -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_main_app),
                            contentDescription = stringResource(R.string.app_logo_description),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(80.dp)
                        )
                        Text(
                            text = stringResource(R.string.slogan),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    // Exchange Rates
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.buy_rate, buyRate),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = stringResource(R.string.sell_rate, sellRate),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.you_have),
                                style = MaterialTheme.typography.titleSmall
                            )
                            OutlinedTextField(
                                value = amountToConvert,
                                onValueChange = { exchangeRateViewModel.updateAmountToConvert(it) },
                                label = { Text(stringResource(R.string.amount)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = sourceCurrency,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }

                    IconButton(
                        onClick = { exchangeRateViewModel.swapCurrencies() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.exchange_icon),
                            contentDescription = stringResource(R.string.swap_currencies)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.you_receive),
                                style = MaterialTheme.typography.titleSmall
                            )
                            OutlinedTextField(
                                value = convertedAmount,
                                onValueChange = {  },
                                label = { Text(stringResource(R.string.converted_amount)) },
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = targetCurrency,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            exchangeRateViewModel.saveConversion()
                        }

                    ) {
                        Text(
                            text = stringResource(R.string.save_conversion),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = { navigateConversion() }

                    ) {
                        Text(
                            text = stringResource(R.string.conversion_saved),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                is ExchangeRateUiState.Error -> {
                    // ... (Error UI)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ExchangeRateTheme {
        ExchangeRateCalculatorScreen()
    }
}