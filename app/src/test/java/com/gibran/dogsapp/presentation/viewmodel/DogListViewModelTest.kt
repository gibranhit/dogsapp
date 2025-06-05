package com.gibran.dogsapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.domain.usecase.GetDogsUseCase
import com.gibran.dogsapp.presentation.event.DogListUiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class DogListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val mockGetDogsUseCase = mockk<GetDogsUseCase>()
    private lateinit var viewModel: DogListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DogListViewModel(mockGetDogsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() {
        // Arrange
        // (viewModel already initialized in setup)

        // Act
        val initialState = viewModel.uiState.value

        // Assert
        assertTrue(initialState.isLoading)
        assertTrue(initialState.dogs.isEmpty())
        assertNull(initialState.errorMessage)
    }

    @Test
    fun `handleEvent LoadDogs should load dogs successfully`() = runTest {
        // Arrange
        val mockDogs = listOf(
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
        coEvery { mockGetDogsUseCase() } returns mockDogs

        // Act
        viewModel.handleEvent(DogListUiEvent.LoadDogs)
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(mockDogs, finalState.dogs)
        assertNull(finalState.errorMessage)
        coVerify { mockGetDogsUseCase() }
    }

    @Test
    fun `handleEvent LoadDogs should handle error correctly`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { mockGetDogsUseCase() } throws Exception(errorMessage)

        // Act
        viewModel.handleEvent(DogListUiEvent.LoadDogs)
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.dogs.isEmpty())
        assertEquals(errorMessage, finalState.errorMessage)
        coVerify { mockGetDogsUseCase() }
    }

    @Test
    fun `handleEvent LoadDogs should handle unknown error`() = runTest {
        // Arrange
        coEvery { mockGetDogsUseCase() } throws Exception()

        // Act
        viewModel.handleEvent(DogListUiEvent.LoadDogs)
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.dogs.isEmpty())
        assertEquals("Unknown error", finalState.errorMessage)
        coVerify { mockGetDogsUseCase() }
    }

    @Test
    fun `handleEvent RetryLoadDogs should reload dogs`() = runTest {
        // Arrange
        val mockDogs = listOf(
            Dog(
                id = 1,
                name = "Max",
                description = "A loyal german shepherd",
                age = 5,
                imageUrl = "https://example.com/dog3.jpg"
            )
        )
        coEvery { mockGetDogsUseCase() } returns mockDogs

        // Act
        viewModel.handleEvent(DogListUiEvent.RetryLoadDogs)
        advanceUntilIdle()

        // Assert
        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(mockDogs, finalState.dogs)
        assertNull(finalState.errorMessage)
        coVerify { mockGetDogsUseCase() }
    }

    @Test
    fun `loading state should be set correctly during loadDogs`() = runTest {
        // Arrange
        val mockDogs = listOf(
            Dog(
                id = 1,
                name = "Buddy",
                description = "A friendly dog",
                age = 3,
                imageUrl = "https://example.com/dog1.jpg"
            )
        )
        coEvery { mockGetDogsUseCase() } returns mockDogs

        // Act
        viewModel.handleEvent(DogListUiEvent.LoadDogs)

        // Assert - Check initial loading state
        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
        assertNull(loadingState.errorMessage)

        advanceUntilIdle()

        // Assert - Check final state after loading
        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(mockDogs, finalState.dogs)
        assertNull(finalState.errorMessage)
    }

    @Test
    fun `error state should clear previous error when retrying`() = runTest {
        // Arrange - First call fails
        coEvery { mockGetDogsUseCase() } throws Exception("Network error")

        // Act - First call
        viewModel.handleEvent(DogListUiEvent.LoadDogs)
        advanceUntilIdle()

        // Assert - Verify error state
        val errorState = viewModel.uiState.value
        assertEquals("Network error", errorState.errorMessage)

        // Arrange - Second call succeeds
        val mockDogs = listOf(
            Dog(
                id = 1,
                name = "Buddy",
                description = "A friendly dog",
                age = 3,
                imageUrl = "https://example.com/dog1.jpg"
            )
        )
        coEvery { mockGetDogsUseCase() } returns mockDogs

        // Act - Second call (retry)
        viewModel.handleEvent(DogListUiEvent.RetryLoadDogs)
        advanceUntilIdle()

        // Assert - Verify success state
        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertEquals(mockDogs, successState.dogs)
        assertNull(successState.errorMessage)
    }
}
