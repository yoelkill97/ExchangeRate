package com.yoelkill.exchangerate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yoelkill.exchangerate.R
import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.ui.util.formatTimestamp
import com.yoelkill.exchangerate.ui.viewmodel.ConversionHistoryModel
import com.yoelkill.exchangerate.ui.viewmodel.ConversionHistoryUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionHistoryScreen(
    viewModel: ConversionHistoryModel = hiltViewModel()
) {
    val historyUiState by viewModel.conversionHistoryState.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(userMessage) {
        userMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.userMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.converted_amount)) },
                actions = {
                    IconButton(onClick = { viewModel.fetchConversionHistory() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh History")
                    }
                    when (val state = historyUiState) {
                        is ConversionHistoryUiState.Success -> {
                            if (state.history.isNotEmpty()) {
                                IconButton(onClick = {

                                    viewModel.onClearHistoryClicked()
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Clear History")
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = historyUiState) {
                is ConversionHistoryUiState.Loading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                }

                is ConversionHistoryUiState.Success -> {
                    if (state.history.isEmpty()) {
                        Text("No hay historial de conversiones aun.")
                    } else {
                        HistoryList(historyList = state.history)
                    }
                }

                is ConversionHistoryUiState.Empty -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "No hay historial de conversiones aun.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Crea una conversion para ver el historial.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                is ConversionHistoryUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchConversionHistory() }) {
                            Text("Volver Intentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryList(historyList: List<ConversionHistory>) {
    if (historyList.isEmpty()) { // Double check, though Empty state should handle it
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay historial de conversiones aun.")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(historyList.reversed()) { historyItem -> // Show newest first
            HistoryItemCard(historyItem = historyItem)
        }
    }
}

@Composable
fun HistoryItemCard(historyItem: ConversionHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${
                        String.format(
                            "%.2f",
                            historyItem.fromAmount
                        )
                    } ${historyItem.fromCurrency}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = "Convertido a",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "${
                        String.format(
                            "%.2f",
                            historyItem.toAmount
                        )
                    } ${historyItem.toCurrency}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: ${historyItem.timestamp.formatTimestamp()}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
