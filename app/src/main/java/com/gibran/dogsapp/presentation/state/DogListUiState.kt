package com.gibran.dogsapp.presentation.state

import com.gibran.dogsapp.domain.model.Dog

data class DogListUiState(
    val dogs: List<Dog> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
