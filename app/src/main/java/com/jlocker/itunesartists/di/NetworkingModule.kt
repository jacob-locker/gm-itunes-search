package com.jlocker.itunesartists.di

import com.jlocker.itunesartists.interfaces.IArtistsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkingModule {
    @Singleton
    @Provides
    fun provideArtistsService(): IArtistsService = IArtistsService.create()
}