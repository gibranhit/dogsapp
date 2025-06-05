package com.gibran.dogsapp.data.repository

import com.gibran.dogsapp.data.local.DogDao
import com.gibran.dogsapp.data.local.DogEntity
import com.gibran.dogsapp.data.remote.DogApi
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.domain.repository.DogRepository
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val api: DogApi,
    private val dao: DogDao
) : DogRepository {

    override suspend fun getDogs(): List<Dog> {
        val cachedDogs = dao.getAllDogs()

        return if (cachedDogs.isEmpty()) {
            val dogsFromApi = api.getDogs().map { it.toDomain() }
            val dogsToCache = dogsFromApi.map { DogEntity.fromDomain(it) }
            dao.insertAll(dogsToCache)
            dogsFromApi
        } else {
            cachedDogs.map { it.toDomain() }
        }
    }
}
