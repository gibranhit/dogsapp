package com.gibran.dogsapp.presentation.navigation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gibran.dogsapp.MainActivity
import com.gibran.dogsapp.di.AppModule
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.ui.DogDetailScreen
import com.gibran.dogsapp.presentation.ui.DogListContent
import com.gibran.dogsapp.presentation.state.DogListUiState
import com.gibran.dogsapp.presentation.theme.DogsAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
class DogsNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun navigation_end_to_end_from_list_to_detail_displays_dog_info() {
        // Arrange
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            DogsNavigation(navController)
        }

        // Assert

        //List Screen
        composeTestRule.onNodeWithText("Buddy").assertIsDisplayed()

        composeTestRule.onNodeWithText("Buddy").performClick()

        //Detail Screen
        composeTestRule.onNodeWithTag("dogNameText").assertIsDisplayed()
        composeTestRule.onNodeWithText("About Buddy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Almost 3 years old").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        //List Screen
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buddy").assertIsDisplayed()
    }

    @Test
    fun navigation_list_screen_displays_correctly() {
        // Arrange
        val dogs = listOf(
            Dog(1, "Buddy", "A friendly dog", 3, "https://example.com/1.jpg"),
            Dog(2, "Luna", "A playful dog", 2, "https://example.com/2.jpg")
        )
        val uiState = DogListUiState(dogs = dogs, isLoading = false, errorMessage = null)

        // Act
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogListContent(uiState = uiState)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buddy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Luna").assertIsDisplayed()
    }

    @Test
    fun navigation_detail_screen_displays_correctly() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "NavigationDog",
            description = "A dog for testing navigation display",
            age = 4,
            imageUrl = "https://example.com/nav-dog.jpg"
        )

        // Act
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag("dogNameText").assertIsDisplayed()
        composeTestRule.onNodeWithText("A dog for testing navigation display").assertIsDisplayed()
        composeTestRule.onNodeWithText("About NavigationDog").assertIsDisplayed()
        composeTestRule.onNodeWithText("Almost 4 years old").assertIsDisplayed()
    }

    @Test
    fun navigation_dog_click_callback_triggers() {
        // Arrange
        val testDog = Dog(1, "ClickableDog", "Test dog", 3, "https://example.com/click.jpg")
        val uiState = DogListUiState(dogs = listOf(testDog), isLoading = false, errorMessage = null)
        var clickedDog: Dog? = null

        // Act
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = uiState,
                    onDogClick = { dog -> clickedDog = dog }
                )
            }
        }

        composeTestRule.onNodeWithText("ClickableDog").performClick()

        // Assert
        assert(clickedDog == testDog)
        assert(clickedDog?.name == "ClickableDog")
    }

    @Test
    fun navigation_back_button_callback_triggers() {
        // Arrange
        val testDog = Dog(1, "BackDog", "Test back navigation", 5, "https://example.com/back.jpg")
        var backClicked = false

        // Act
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = { backClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Assert
        assert(backClicked)
    }

    @Test
    fun navigation_data_passes_correctly_between_screens() {
        // Arrange
        val originalDog = Dog(
            id = 42,
            name = "DataTransferDog",
            description = "Testing data transfer between screens",
            age = 6,
            imageUrl = "https://example.com/data.jpg"
        )

        // Act - Simulate data passed from list to detail
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = originalDog,
                    onBackClick = {}
                )
            }
        }

        // Assert - Verify all data is correctly displayed
        composeTestRule.onNodeWithTag("dogNameText").assertIsDisplayed()
        composeTestRule.onNodeWithText("Testing data transfer between screens").assertIsDisplayed()
        composeTestRule.onNodeWithText("Almost 6 years old").assertIsDisplayed()
        composeTestRule.onNodeWithText("About DataTransferDog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ageGroupText").assertIsDisplayed()
    }

    @Test
    fun navigation_error_state_displays_retry_option() {
        // Arrange
        val errorState = DogListUiState(
            dogs = emptyList(),
            isLoading = false,
            errorMessage = "Failed to load dogs"
        )
        var retryClicked = false

        // Act
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogListContent(
                    uiState = errorState,
                    onEvent = { retryClicked = true }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Failed to load dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()

        // Test retry functionality
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryClicked)
    }

    @Test
    fun navigation_loading_state_displays_correctly() {
        // Arrange
        val loadingState = DogListUiState(
            dogs = emptyList(),
            isLoading = true,
            errorMessage = null
        )

        // Act
        composeTestRule.activity.setContent {
            DogsAppTheme {
                DogListContent(uiState = loadingState)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Dogs We Love").assertIsDisplayed()
        // Loading state should show title and shimmer cards (no specific test for shimmer)
    }
}
