package com.gibran.dogsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibran.dogsapp.domain.usecase.GetDogsUseCase
import com.gibran.dogsapp.presentation.event.DogListUiEvent
import com.gibran.dogsapp.presentation.state.DogListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DogListViewModel @Inject constructor(
    private val getDogsUseCase: GetDogsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DogListUiState(isLoading = true))
    val uiState: StateFlow<DogListUiState> = _uiState.asStateFlow()

    fun handleEvent(event: DogListUiEvent) {
        when (event) {
            is DogListUiEvent.LoadDogs -> loadDogs()
            is DogListUiEvent.RetryLoadDogs -> loadDogs()
        }
    }

    private fun loadDogs() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                val dogs = getDogsUseCase()
                _uiState.value = _uiState.value.copy(
                    dogs = dogs,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Unknown error"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = errorMessage
                )
            }
        }
    }
}
