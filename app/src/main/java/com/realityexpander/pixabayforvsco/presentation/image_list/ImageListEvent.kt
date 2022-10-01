package com.realityexpander.pixabayforvsco.presentation.image_list

sealed class ImageListEvent{
    object OnRefresh : ImageListEvent()
    data class OnSearchQueryChanged(val query: String) : ImageListEvent()
}
