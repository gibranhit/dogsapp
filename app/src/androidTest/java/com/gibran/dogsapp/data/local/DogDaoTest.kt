package com.gibran.dogsapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DogDaoTest {

    private lateinit var database: DogDatabase
    private lateinit var dogDao: DogDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DogDatabase::class.java
        ).allowMainThreadQueries().build()
        dogDao = database.dogDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAll_and_getAllDogs_should_work_correctly() = runTest {
        // Arrange
        val dogs = listOf(
            DogEntity(
                id = 1,
                name = "Buddy",
                description = "A friendly golden retriever",
                age = 3,
                imageUrl = "https://example.com/dog1.jpg"
            ),
            DogEntity(
                id = 2,
                name = "Luna",
                description = "A playful husky",
                age = 2,
                imageUrl = "https://example.com/dog2.jpg"
            )
        )

        // Act
        dogDao.insertAll(dogs)
        val result = dogDao.getAllDogs()

        // Assert
        assertEquals(2, result.size)
        assertEquals(dogs[0], result[0])
        assertEquals(dogs[1], result[1])
    }

    @Test
    fun getAllDogs_should_return_empty_list_when_no_data() = runTest {
        // Arrange
        // (empty database)

        // Act
        val result = dogDao.getAllDogs()

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertAll_should_replace_existing_dogs_with_same_id() = runTest {
        // Arrange
        val originalDog = DogEntity(
            id = 1,
            name = "Buddy",
            description = "Original description",
            age = 3,
            imageUrl = "https://example.com/original.jpg"
        )
        val updatedDog = DogEntity(
            id = 1,
            name = "Buddy Updated",
            description = "Updated description",
            age = 4,
            imageUrl = "https://example.com/updated.jpg"
        )

        // Act
        dogDao.insertAll(listOf(originalDog))
        dogDao.insertAll(listOf(updatedDog))
        val result = dogDao.getAllDogs()

        // Assert
        assertEquals(1, result.size)
        assertEquals(updatedDog, result[0])
        assertEquals("Buddy Updated", result[0].name)
        assertEquals("Updated description", result[0].description)
        assertEquals(4, result[0].age)
    }

    @Test
    fun insertAll_should_handle_empty_list() = runTest {
        // Arrange
        val emptyList = emptyList<DogEntity>()

        // Act
        dogDao.insertAll(emptyList)
        val result = dogDao.getAllDogs()

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertAll_should_handle_single_dog() = runTest {
        // Arrange
        val singleDog = listOf(
            DogEntity(
                id = 1,
                name = "Solo",
                description = "A single dog",
                age = 5,
                imageUrl = "https://example.com/solo.jpg"
            )
        )

        // Act
        dogDao.insertAll(singleDog)
        val result = dogDao.getAllDogs()

        // Assert
        assertEquals(1, result.size)
        assertEquals(singleDog[0], result[0])
    }

    @Test
    fun insertAll_should_preserve_insertion_order() = runTest {
        // Arrange
        val dogs = listOf(
            DogEntity(
                id = 3,
                name = "Third",
                description = "Third dog",
                age = 3,
                imageUrl = "url3"
            ),
            DogEntity(
                id = 1,
                name = "First",
                description = "First dog",
                age = 1,
                imageUrl = "url1"
            ),
            DogEntity(
                id = 2,
                name = "Second",
                description = "Second dog",
                age = 2,
                imageUrl = "url2"
            )
        )

        // Act
        dogDao.insertAll(dogs)
        val result = dogDao.getAllDogs()

        // Assert
        assertEquals(3, result.size)
        // Verify dogs are returned in the same order as inserted
        assertEquals("First", result[0].name)
        assertEquals("Second", result[1].name)
        assertEquals("Third", result[2].name)
    }

    @Test
    fun multiple_insertAll_calls_should_accumulate_different_dogs() = runTest {
        // Arrange
        val firstBatch = listOf(
            DogEntity(
                id = 1,
                name = "Dog1",
                description = "First batch",
                age = 1,
                imageUrl = "url1"
            )
        )
        val secondBatch = listOf(
            DogEntity(
                id = 2,
                name = "Dog2",
                description = "Second batch",
                age = 2,
                imageUrl = "url2"
            )
        )

        // Act
        dogDao.insertAll(firstBatch)
        dogDao.insertAll(secondBatch)
        val result = dogDao.getAllDogs()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Dog1" })
        assertTrue(result.any { it.name == "Dog2" })
    }
}
