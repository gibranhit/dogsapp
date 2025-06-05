package com.gibran.dogsapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object DogListRoute

@Serializable
data class DogDetailRoute(
    val dogId: Int,
    val dogName: String,
    val dogDescription: String,
    val dogAge: Int,
    val dogImageUrl: String
)