package com.realityexpander.pixabayforvsco.di

import android.app.Application
import androidx.room.Room
import com.realityexpander.pixabayforvsco.data.local.PixabayDatabase
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePixabayApi():  PixabayApi {
        return Retrofit.Builder()
            .baseUrl(PixabayApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())  // json->kotlin data classes
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providePixabayDatabase(app: Application) : PixabayDatabase {
        return Room.databaseBuilder(app,
                PixabayDatabase::class.java,
                "pixabay.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}