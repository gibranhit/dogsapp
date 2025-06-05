package com.gibran.dogsapp.domain.usecase

import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.domain.repository.DogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetDogsUseCaseTest {

    private val mockRepository = mockk<DogRepository>()
    private lateinit var useCase: GetDogsUseCase

    @Before
    fun setup() {
        useCase = GetDogsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return dogs from repository`() = runTest {
        // Arrange
        val expectedDogs = listOf(
            Dog(
                id = 1,
                name = "Buddy",
                description = "A friendly golden retriever",
                age = 3,
                imageUrl = "https://example.com/dog1.jpg"
            ),
            Dog(
                id = 2,
                name = "Luna",
                description = "A playful husky",
                age = 2,
                imageUrl = "https://example.com/dog2.jpg"
            )
        )
        coEvery { mockRepository.getDogs() } returns expectedDogs

        // Act
        val result = useCase()

        // Assert
        assertEquals(expectedDogs, result)
        coVerify { mockRepository.getDogs() }
    }

    @Test
    fun `invoke should throw exception when repository fails`() = runTest {
        // Arrange
        val expectedException = Exception("Network error")
        coEvery { mockRepository.getDogs() } throws expectedException

        // Act & Assert
        try {
            useCase()
            fail("Expected exception was not thrown")
        } catch (e: Exception) {
            assertEquals("Network error", e.message)
        }

        coVerify { mockRepository.getDogs() }
    }

    @Test
    fun `invoke should return empty list when repository returns empty list`() = runTest {
        // Arrange
        val emptyList = emptyList<Dog>()
        coEvery { mockRepository.getDogs() } returns emptyList

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isEmpty())
        coVerify { mockRepository.getDogs() }
    }
}
