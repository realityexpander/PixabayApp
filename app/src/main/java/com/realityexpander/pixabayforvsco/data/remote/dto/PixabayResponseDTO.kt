package com.realityexpander.pixabayforvsco.data.remote.dto

import com.realityexpander.pixabayforvsco.domain.model.PixabayImage

data class PixabayResponseDTO(
    val total: Int = 0,
    val totalHits: Int = 0,
    val hits: List<PixabayImage> = emptyList()
)