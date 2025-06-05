package com.gibran.dogsapp.domain.usecase

import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.domain.repository.DogRepository
import javax.inject.Inject

class GetDogsUseCase @Inject constructor(
    private val repository: DogRepository
) {
    suspend operator fun invoke(): List<Dog> {
        return repository.getDogs()
    }
}
