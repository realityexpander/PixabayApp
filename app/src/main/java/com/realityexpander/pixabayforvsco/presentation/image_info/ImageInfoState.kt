package com.realityexpander.pixabayforvsco.presentation.image_info

import com.realityexpander.pixabayforvsco.domain.model.PixabayImage

data class ImageInfoState(
    val pixabayImage: PixabayImage? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
