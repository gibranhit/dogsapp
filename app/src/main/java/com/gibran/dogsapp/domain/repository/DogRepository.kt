package com.gibran.dogsapp.domain.repository

import com.gibran.dogsapp.domain.model.Dog


interface DogRepository {
    suspend fun getDogs(): List<Dog>
}
