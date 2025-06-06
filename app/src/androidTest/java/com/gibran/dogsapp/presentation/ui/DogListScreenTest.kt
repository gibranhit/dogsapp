package com.gibran.dogsapp.presentation.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.event.DogListUiEvent
import com.gibran.dogsapp.presentation.state.DogListUiState
import com.gibran.dogsapp.presentation.theme.DogsAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(AndroidJUnit4::class)
class DogListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun dogListContent_displays_loading_state_correctly() {
        // Arrange
        val loadingState = DogListUiState(isLoading = true)

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = loadingState
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
    }

    @Test
    fun dogListContent_displays_error_state_correctly() {
        // Arrange
        val errorMessage = "Network error occurred"
        val errorState = DogListUiState(
            isLoading = false,
            errorMessage = errorMessage
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = errorState
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun dogListContent_displays_dogs_list_correctly() {
        // Arrange
        val dogs = listOf(
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
        val successState = DogListUiState(
            dogs = dogs,
            isLoading = false,
            errorMessage = null
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = successState
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buddy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Luna").assertIsDisplayed()
        composeTestRule.onNodeWithText("A friendly golden retriever").assertIsDisplayed()
        composeTestRule.onNodeWithText("A playful husky").assertIsDisplayed()
    }

    @Test
    fun dogListContent_retry_button_triggers_event() {
        // Arrange
        val errorState = DogListUiState(
            isLoading = false,
            errorMessage = "Network error"
        )
        var retryEventTriggered = false

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = errorState,
                    onEvent = { event ->
                        if (event is DogListUiEvent.RetryLoadDogs) {
                            retryEventTriggered = true
                        }
                    }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryEventTriggered)
    }

    @Test
    fun dogListContent_back_button_triggers_callback() {
        // Arrange
        val successState = DogListUiState(
            dogs = emptyList(),
            isLoading = false,
            errorMessage = null
        )
        var backPressedTriggered = false

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = successState,
                    onBackPressed = { backPressedTriggered = true }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backPressedTriggered)
    }

    @Test
    fun dogListContent_dog_click_triggers_callback() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "Clickable Dog",
            description = "A dog that can be clicked",
            age = 3,
            imageUrl = "https://example.com/clickable.jpg"
        )
        val successState = DogListUiState(
            dogs = listOf(testDog),
            isLoading = false,
            errorMessage = null
        )
        var clickedDog: Dog? = null

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = successState,
                    onDogClick = { dog -> clickedDog = dog }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Clickable Dog").performClick()
        assert(clickedDog == testDog)
    }

    @Test
    fun dogListContent_empty_list_shows_title_only() {
        // Arrange
        val emptyState = DogListUiState(
            dogs = emptyList(),
            isLoading = false,
            errorMessage = null
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = emptyState
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
    }
}
