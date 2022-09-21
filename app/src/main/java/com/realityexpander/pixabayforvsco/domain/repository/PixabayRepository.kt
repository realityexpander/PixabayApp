package com.realityexpander.pixabayforvsco.domain.repository

import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayResponse
import com.realityexpander.pixabayforvsco.util.Resource

interface PixabayRepository {

    suspend fun getPixabayImages(
        searchString: String,
    ): Resource<PixabayResponse>

}