package com.gibran.dogsapp.data.remote

import com.gibran.dogsapp.data.model.DogResponse
import retrofit2.http.GET

interface DogApi {
    @GET("1151549092634943488")
    suspend fun getDogs(): List<DogResponse>
}
