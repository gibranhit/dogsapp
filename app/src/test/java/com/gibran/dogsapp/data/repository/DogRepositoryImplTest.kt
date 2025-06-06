package com.gibran.dogsapp.data.repository

import com.gibran.dogsapp.data.local.DogDao
import com.gibran.dogsapp.data.local.DogEntity
import com.gibran.dogsapp.data.model.DogResponse
import com.gibran.dogsapp.data.remote.DogApi
import com.gibran.dogsapp.domain.model.Dog
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DogRepositoryImplTest {

    private val mockApi = mockk<DogApi>()
    private val mockDao = mockk<DogDao>()
    private lateinit var repository: DogRepositoryImpl
    private val ioDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        repository = DogRepositoryImpl(mockApi, mockDao, ioDispatcher)
    }

    @Test
    fun `getDogs should return cached dogs when cache is not empty`() = runTest {
        // Arrange
        val cachedDogEntities = listOf(
            DogEntity(1, "Buddy", "A friendly golden retriever", 3, "https://example.com/dog1.jpg"),
            DogEntity(2, "Luna", "A playful husky", 2, "https://example.com/dog2.jpg")
        )
        val expectedDogs = cachedDogEntities.map { it.toDomain() }
        coEvery { mockDao.getAllDogs() } returns cachedDogEntities

        // Act
        val result = repository.getDogs()

        // Assert
        assertEquals(expectedDogs, result)
        coVerify { mockDao.getAllDogs() }
        coVerify(exactly = 0) { mockApi.getDogs() }
        coVerify(exactly = 0) { mockDao.insertAll(any()) }
    }

    @Test
    fun `getDogs should fetch from API and cache when cache is empty`() = runTest {
        // Arrange
        val emptyCache = emptyList<DogEntity>()
        val apiDogResponses = listOf(
            DogResponse("Buddy", "A friendly golden retriever", 3, "https://example.com/dog1.jpg"),
            DogResponse("Luna", "A playful husky", 2, "https://example.com/dog2.jpg")
        )
        val expectedDogs = apiDogResponses.map { it.toDomain() }
        val expectedCacheEntities = expectedDogs.map { DogEntity.fromDomain(it) }

        coEvery { mockDao.getAllDogs() } returns emptyCache
        coEvery { mockApi.getDogs() } returns apiDogResponses
        coEvery { mockDao.insertAll(any()) } returns Unit

        // Act
        val result = repository.getDogs()

        // Assert
        assertEquals(expectedDogs, result)
        coVerify { mockDao.getAllDogs() }
        coVerify { mockApi.getDogs() }
        coVerify { mockDao.insertAll(expectedCacheEntities) }
    }

    @Test
    fun `getDogs should throw exception when API call fails and cache is empty`() = runTest {
        // Arrange
        val emptyCache = emptyList<DogEntity>()
        val expectedException = Exception("Network error")

        coEvery { mockDao.getAllDogs() } returns emptyCache
        coEvery { mockApi.getDogs() } throws expectedException

        // Act & Assert
        try {
            repository.getDogs()
            fail("Expected exception was not thrown")
        } catch (e: Exception) {
            assertEquals("Network error", e.message)
        }

        coVerify { mockDao.getAllDogs() }
        coVerify { mockApi.getDogs() }
        coVerify(exactly = 0) { mockDao.insertAll(any()) }
    }

    @Test
    fun `getDogs should return empty list when API returns empty list and cache is empty`() =
        runTest {
            // Arrange
            val emptyCache = emptyList<DogEntity>()
            val emptyApiResponse = emptyList<DogResponse>()

            coEvery { mockDao.getAllDogs() } returns emptyCache
            coEvery { mockApi.getDogs() } returns emptyApiResponse
            coEvery { mockDao.insertAll(any()) } returns Unit

            // Act
            val result = repository.getDogs()

            // Assert
            assertTrue(result.isEmpty())
            coVerify { mockDao.getAllDogs() }
            coVerify { mockApi.getDogs() }
            coVerify { mockDao.insertAll(emptyList()) }
        }

    @Test
    fun `getDogs should throw exception when DAO fails to retrieve cached data`() = runTest {
        // Arrange
        val expectedException = Exception("Database error")
        coEvery { mockDao.getAllDogs() } throws expectedException

        // Act & Assert
        try {
            repository.getDogs()
            fail("Expected exception was not thrown")
        } catch (e: Exception) {
            assertEquals("Database error", e.message)
        }

        coVerify { mockDao.getAllDogs() }
        coVerify(exactly = 0) { mockApi.getDogs() }
        coVerify(exactly = 0) { mockDao.insertAll(any()) }
    }
}