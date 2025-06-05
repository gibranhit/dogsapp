package com.gibran.dogsapp.data.local

import com.gibran.dogsapp.domain.model.Dog
import org.junit.Assert.*
import org.junit.Test

class DogEntityTest {

    @Test
    fun `toDomain should map DogEntity to Dog correctly`() {
        // Arrange
        val dogEntity = DogEntity(
            id = 1,
            name = "Buddy",
            description = "A friendly golden retriever",
            age = 3,
            imageUrl = "https://example.com/dog1.jpg"
        )

        // Act
        val result = dogEntity.toDomain()

        // Assert
        val expected = Dog(
            id = 1,
            name = "Buddy",
            description = "A friendly golden retriever",
            age = 3,
            imageUrl = "https://example.com/dog1.jpg"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `fromDomain should map Dog to DogEntity correctly`() {
        // Arrange
        val dog = Dog(
            id = 1,
            name = "Luna",
            description = "A playful husky",
            age = 2,
            imageUrl = "https://example.com/dog2.jpg"
        )

        // Act
        val result = DogEntity.fromDomain(dog)

        // Assert
        val expected = DogEntity(
            id = 1,
            name = "Luna",
            description = "A playful husky",
            age = 2,
            imageUrl = "https://example.com/dog2.jpg"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `roundtrip conversion should preserve data`() {
        // Arrange
        val originalDog = Dog(
            id = 5,
            name = "Max",
            description = "A loyal german shepherd",
            age = 4,
            imageUrl = "https://example.com/dog3.jpg"
        )

        // Act
        val entity = DogEntity.fromDomain(originalDog)
        val convertedBack = entity.toDomain()

        // Assert
        assertEquals(originalDog, convertedBack)
    }

    @Test
    fun `toDomain should handle zero id correctly`() {
        // Arrange
        val dogEntity = DogEntity(
            id = 0,
            name = "Puppy",
            description = "A new puppy",
            age = 1,
            imageUrl = "https://example.com/puppy.jpg"
        )

        // Act
        val result = dogEntity.toDomain()

        // Assert
        val expected = Dog(
            id = 0,
            name = "Puppy",
            description = "A new puppy",
            age = 1,
            imageUrl = "https://example.com/puppy.jpg"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `fromDomain should handle empty strings correctly`() {
        // Arrange
        val dog = Dog(
            id = 2,
            name = "",
            description = "",
            age = 0,
            imageUrl = ""
        )

        // Act
        val result = DogEntity.fromDomain(dog)

        // Assert
        val expected = DogEntity(
            id = 2,
            name = "",
            description = "",
            age = 0,
            imageUrl = ""
        )
        assertEquals(expected, result)
    }
}