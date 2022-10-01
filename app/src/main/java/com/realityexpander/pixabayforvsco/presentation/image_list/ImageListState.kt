package com.realityexpander.pixabayforvsco.presentation.image_list

import com.realityexpander.pixabayforvsco.domain.model.PixabayImage

data class ImageListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val pixabayImageList: List<PixabayImage> = emptyList(),
    val endReached: Boolean = false,
    val page: Int = 1,
    val totalHits: Int = 0,
    val maxCachedPage: Int = 0
)
