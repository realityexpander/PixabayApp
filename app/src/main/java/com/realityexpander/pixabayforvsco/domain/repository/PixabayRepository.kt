package com.realityexpander.pixabayforvsco.domain.repository

import com.realityexpander.pixabayforvsco.domain.model.PixabayImage
import com.realityexpander.pixabayforvsco.util.Resource
import kotlinx.coroutines.flow.Flow

interface PixabayRepository {

    suspend fun getPixabayImages(
        isFetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<PixabayImage>>>

    suspend fun getPixabayImage(id: String): Resource<PixabayImage>

    suspend fun getNextPagePixabayImages(
        query: String,
        page: Int,
        perPage: Int = 20
    ): Flow<Resource<List<PixabayImage>>>

    suspend fun clearCacheForQuery(query: String)
}