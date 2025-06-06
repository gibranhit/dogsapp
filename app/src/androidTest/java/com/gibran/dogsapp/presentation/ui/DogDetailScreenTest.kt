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
import com.gibran.dogsapp.presentation.theme.DogsAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(AndroidJUnit4::class)
class DogDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun dogDetailScreen_displays_dog_description() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "Buddy",
            description = "A friendly golden retriever who loves to play fetch and swim in the lake",
            age = 3,
            imageUrl = "https://example.com/dog1.jpg"
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = {}
                )
            }
        }

        // Assert - Check if description is displayed
        composeTestRule.onNodeWithText("A friendly golden retriever who loves to play fetch and swim in the lake")
            .assertIsDisplayed()
    }

    @Test
    fun dogDetailScreen_back_button_works() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "Luna",
            description = "A playful husky",
            age = 2,
            imageUrl = "https://example.com/dog2.jpg"
        )
        var backClickTriggered = false

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = { backClickTriggered = true }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backClickTriggered)
    }

    @Test
    fun dogDetailScreen_shows_age_information() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "TestDog",
            description = "A test dog",
            age = 5,
            imageUrl = "https://example.com/test.jpg"
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Almost 5 years old").assertIsDisplayed()
    }

    @Test
    fun dogDetailScreen_shows_about_section() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "Charlie",
            description = "A wonderful companion",
            age = 4,
            imageUrl = "https://example.com/charlie.jpg"
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("About Charlie").assertIsDisplayed()
        composeTestRule.onNodeWithText("A wonderful companion").assertIsDisplayed()
    }

    @Test
    fun dogDetailScreen_shows_personality_and_age_group_labels() {
        // Arrange
        val testDog = Dog(
            id = 1,
            name = "Max",
            description = "A loyal companion",
            age = 6,
            imageUrl = "https://example.com/max.jpg"
        )

        // Act
        composeTestRule.setContent {
            DogsAppTheme {
                DogDetailScreen(
                    dog = testDog,
                    onBackClick = {}
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Personality").assertIsDisplayed()
        composeTestRule.onNodeWithText("Age Group").assertIsDisplayed()
    }
}
