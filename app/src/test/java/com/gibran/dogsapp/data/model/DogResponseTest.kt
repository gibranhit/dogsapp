package com.gibran.dogsapp.data.model

import com.gibran.dogsapp.domain.model.Dog
import org.junit.Assert.*
import org.junit.Test

class DogResponseTest {

    @Test
    fun `toDomain should map DogResponse to Dog correctly`() {
        // Arrange
        val dogResponse = DogResponse(
            name = "Buddy",
            description = "A friendly golden retriever",
            age = 3,
            imageUrl = "https://example.com/dog1.jpg"
        )

        // Act
        val result = dogResponse.toDomain()

        // Assert
        val expected = Dog(
            id = 0, // API doesn't provide ID, should default to 0
            name = "Buddy",
            description = "A friendly golden retriever",
            age = 3,
            imageUrl = "https://example.com/dog1.jpg"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toDomain should handle empty strings correctly`() {
        // Arrange
        val dogResponse = DogResponse(
            name = "",
            description = "",
            age = 0,
            imageUrl = ""
        )

        // Act
        val result = dogResponse.toDomain()

        // Assert
        val expected = Dog(
            id = 0,
            name = "",
            description = "",
            age = 0,
            imageUrl = ""
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toDomain should handle special characters in strings`() {
        // Arrange
        val dogResponse = DogResponse(
            name = "Café's Dog",
            description = "A dog with café vibes & special chars!",
            age = 5,
            imageUrl = "https://example.com/dog-café.jpg"
        )

        // Act
        val result = dogResponse.toDomain()

        // Assert
        val expected = Dog(
            id = 0,
            name = "Café's Dog",
            description = "A dog with café vibes & special chars!",
            age = 5,
            imageUrl = "https://example.com/dog-café.jpg"
        )
        assertEquals(expected, result)
    }
}