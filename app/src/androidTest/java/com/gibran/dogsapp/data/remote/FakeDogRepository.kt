package com.gibran.dogsapp.data.remote

import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.domain.repository.DogRepository

class FakeDogRepository : DogRepository {
    override suspend fun getDogs(): List<Dog> {
        return listOf(
            Dog(1, "Buddy", "A friendly dog", 3, "https://example.com/1.jpg"),
            Dog(2, "Luna", "A playful dog", 2, "https://example.com/2.jpg")
        )
    }
}
