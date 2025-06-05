package com.gibran.dogsapp.data.model

import com.gibran.dogsapp.domain.model.Dog
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DogResponse (
    @Json(name = "dogName") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "age") val age: Int,
    @Json(name = "image") val imageUrl: String
) {
    fun toDomain(): Dog = Dog(
        id = 0, // API doesn't provide ID, so we use 0 - Room will auto-generate when saving
        name = name,
        description = description,
        age = age,
        imageUrl = imageUrl
    )
}
