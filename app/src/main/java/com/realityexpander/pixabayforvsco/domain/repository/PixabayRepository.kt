package com.realityexpander.pixabayforvsco.domain.repository

import com.realityexpander.pixabayforvsco.domain.model.PixabayImage
import com.realityexpander.pixabayforvsco.util.Resource
import kotlinx.coroutines.flow.Flow

interface PixabayRepository {

    suspend fun getPixabayImages(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<PixabayImage>>>

    suspend fun getPixabayImage(id: String): Resource<PixabayImage>

}