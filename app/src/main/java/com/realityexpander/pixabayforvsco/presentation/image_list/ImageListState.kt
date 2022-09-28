package com.realityexpander.pixabayforvsco.presentation.image_list

import com.realityexpander.pixabayforvsco.domain.model.PixabayImage

data class ImageListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val pixabayImageList: List<PixabayImage> = emptyList(),
)
