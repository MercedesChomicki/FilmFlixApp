package com.cursokotlin.movieapp.di

import com.cursokotlin.movieapp.BuildConfig
import com.cursokotlin.movieapp.ddl.data.remote.MovieApi
import com.cursokotlin.movieapp.ddl.data.remote.MovieRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(movieApi: MovieApi): MovieRemoteDataSource {
        return MovieRemoteDataSource(movieApi)
    }
}