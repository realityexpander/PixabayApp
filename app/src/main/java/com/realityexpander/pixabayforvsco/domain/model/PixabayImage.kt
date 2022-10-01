package com.realityexpander.pixabayforvsco.domain.model

data class PixabayImage(
    val id: String = "",
    val pageURL: String = "",
    val type: String = "",
    val tags: String = "",
    val previewURL: String = "",
    val previewWidth: Int = 0,
    val previewHeight: Int = 0,
    val webformatURL: String = "",
    val webformatWidth: Int = 0,
    val webformatHeight: Int = 0,
    val largeImageURL: String = "",
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val imageSize: Int = 0,
    val views: Int = 0,
    val downloads: Int = 0,
    val favorites: Int = 0,
    val likes: Int = 0,
    val comments: Int = 0,
    val user_id: Int = 0,
    val user: String = "",
    val userImageURL: String = "",
    val page: Int = 0
)