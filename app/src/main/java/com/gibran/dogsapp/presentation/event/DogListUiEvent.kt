package com.gibran.dogsapp.presentation.event

sealed class DogListUiEvent {
    object LoadDogs : DogListUiEvent()
    object RetryLoadDogs : DogListUiEvent()
}
