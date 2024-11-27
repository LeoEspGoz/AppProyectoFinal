package com.example.appnotas.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnotas.data.Nota
import com.example.appnotas.data.NotasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(notasRepository: NotasRepository) : ViewModel(){
    val homeUiState: StateFlow<HomeUiState> =
            notasRepository.getAllNotasStream().map { HomeUiState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = HomeUiState()
                )
    companion object {
        private const val TIMEOUT_MILLIS = 5_00L
    }
}
data class HomeUiState(val notaList: List<Nota> = listOf())