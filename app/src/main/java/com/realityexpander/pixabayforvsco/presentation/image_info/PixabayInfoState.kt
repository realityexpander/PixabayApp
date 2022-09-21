package com.realityexpander.pixabayforvsco.presentation.image_info

import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayImage

data class PixabayInfoState(
    val pixabayImage: PixabayImage? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
