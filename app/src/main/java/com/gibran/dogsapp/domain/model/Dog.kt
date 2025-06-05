package com.gibran.dogsapp.domain.model

data class Dog(
    val id: Int = 0,
    val name: String,
    val description: String,
    val age: Int,
    val imageUrl: String
)
