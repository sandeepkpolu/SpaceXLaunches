package com.beweaver.spacexlaunches.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("base-url")
    fun provideSpaceXUrl(): String = "https://spacex-production.up.railway.app/"

    @Provides
    @Singleton
    fun provideApolloClient(@Named("base-url") baseUrl: String): ApolloClient {
        return ApolloClient.Builder().serverUrl(baseUrl).build()
    }

}