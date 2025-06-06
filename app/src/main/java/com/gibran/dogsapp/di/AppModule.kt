package com.gibran.dogsapp.di

import android.app.Application
import androidx.room.Room
import com.gibran.dogsapp.BuildConfig
import com.gibran.dogsapp.data.local.DogDao
import com.gibran.dogsapp.data.local.DogDatabase
import com.gibran.dogsapp.data.remote.DogApi
import com.gibran.dogsapp.data.repository.DogRepositoryImpl
import com.gibran.dogsapp.domain.repository.DogRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideDogApi(retrofit: Retrofit): DogApi =
        retrofit.create(DogApi::class.java)

    @Provides
    @Singleton
    fun provideDogDatabase(app: Application): DogDatabase =
        Room.databaseBuilder(app, DogDatabase::class.java, BuildConfig.NAME_DATABASE).build()

    @Provides
    @Singleton
    fun provideDogDao(database: DogDatabase): DogDao = database.dogDao()

    @OptIn(ExperimentalCoroutinesApi::class)
    @DispatchersIO
    @Provides
    fun providesDispatchersIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDogRepository(
        api: DogApi,
        dao: DogDao,
        @DispatchersIO dispatcher: CoroutineDispatcher
    ): DogRepository =
        DogRepositoryImpl(api, dao, dispatcher)
}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DispatchersIO