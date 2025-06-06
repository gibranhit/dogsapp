package com.gibran.dogsapp.di

import com.gibran.dogsapp.data.remote.FakeDogRepository
import com.gibran.dogsapp.domain.repository.DogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDogRepository(): DogRepository = FakeDogRepository()
}
