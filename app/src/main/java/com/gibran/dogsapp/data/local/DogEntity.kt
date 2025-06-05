package com.gibran.dogsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gibran.dogsapp.domain.model.Dog

@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val age: Int,
    val imageUrl: String
) {
    fun toDomain(): Dog = Dog(id, name, description, age, imageUrl)

    companion object {
        fun fromDomain(dog: Dog): DogEntity =
            DogEntity(
                id = dog.id,
                name = dog.name,
                description = dog.description,
                age = dog.age,
                imageUrl = dog.imageUrl
            )
    }
}
