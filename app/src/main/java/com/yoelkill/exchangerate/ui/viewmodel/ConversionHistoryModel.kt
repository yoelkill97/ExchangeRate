package com.yoelkill.exchangerate.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoelkill.exchangerate.domain.model.ConversionHistory
import com.yoelkill.exchangerate.domain.usecase.CleanConversionHistoryUseCase
import com.yoelkill.exchangerate.domain.usecase.GetConversionHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionHistoryModel @Inject constructor(
    private val getConversionHistoryUseCase: GetConversionHistoryUseCase,
    private val cleanConversionHistoryUseCase: CleanConversionHistoryUseCase
) : ViewModel() {
    private val _conversionHistoryState =
        MutableStateFlow<ConversionHistoryUiState>(ConversionHistoryUiState.Loading)
    val conversionHistoryState: StateFlow<ConversionHistoryUiState> =
        _conversionHistoryState.asStateFlow()
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()
    init {
        fetchConversionHistory()
    }
    fun fetchConversionHistory() {
        viewModelScope.launch {
            _conversionHistoryState.value = ConversionHistoryUiState.Loading
            delay(500)
            getConversionHistoryUseCase()
                .catch { e ->
                    _conversionHistoryState.value =
                        ConversionHistoryUiState.Error("Error al obtener el historial de conversiones: ${e.message}")
                }
                .collect { historyList ->
                    if (historyList.isEmpty()) {
                        _conversionHistoryState.value = ConversionHistoryUiState.Empty
                    } else {
                        _conversionHistoryState.value = ConversionHistoryUiState.Success(historyList)
                    }
                }
        }
    }
    fun onClearHistoryClicked() {
        viewModelScope.launch {
            val result = cleanConversionHistoryUseCase.invoke()
            result.onSuccess {
                _conversionHistoryState.value = ConversionHistoryUiState.Empty
                _userMessage.value = "Se Eliminaron las conversiones exitosamente."
            }.onFailure { throwable ->
                _userMessage.value = "Fallo al eliminar las conversiones. ${throwable.message}"
            }
        }
    }
    fun userMessageShown() {
        _userMessage.value = null
    }
}

sealed class ConversionHistoryUiState {
    data class Success(val history: List<ConversionHistory>) : ConversionHistoryUiState()
    data class Error(val message: String) : ConversionHistoryUiState()
    object Loading : ConversionHistoryUiState()
    object Empty : ConversionHistoryUiState()
}