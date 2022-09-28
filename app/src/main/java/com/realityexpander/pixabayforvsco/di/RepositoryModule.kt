package com.realityexpander.pixabayforvsco.di

import com.realityexpander.pixabayforvsco.data.repository.PixabayRepositoryImpl
import com.realityexpander.pixabayforvsco.domain.repository.PixabayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        pixabayRepositoryImpl: PixabayRepositoryImpl
    ): PixabayRepository
}