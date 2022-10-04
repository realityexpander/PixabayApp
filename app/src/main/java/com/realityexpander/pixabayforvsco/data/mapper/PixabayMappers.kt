package com.realityexpander.pixabayforvsco.data.mapper

import com.realityexpander.pixabayforvsco.data.local.PixabayImageEntity
import com.realityexpander.pixabayforvsco.domain.model.PixabayImage

///// Pixabay mappers

fun PixabayImage.toPixabayImageEntity(
    originalSearchTerm: String,
    page: Int = 1,
): PixabayImageEntity {
    return PixabayImageEntity(
        //room_id = room_id,
        id = id,
        pageURL = pageURL,
        type = type,
        tags = tags,
        previewURL = previewURL,
        previewWidth = previewWidth,
        previewHeight = previewHeight,
        webformatURL = webformatURL,
        webformatWidth = webformatWidth,
        webformatHeight = webformatHeight,
        largeImageURL = largeImageURL,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        imageSize = imageSize,
        views = views,
        downloads = downloads,
        favorites = favorites,
        likes = likes,
        comments = comments,
        user_id = user_id,
        user = user,
        userImageURL = userImageURL,
        originalSearchTerm = originalSearchTerm,
        page = page
    )
}

fun PixabayImageEntity.toPixabayImage(): PixabayImage {
    return PixabayImage(
        //room_id = room_id,
        id = id,
        pageURL = pageURL,
        type = type,
        tags = tags,
        previewURL = previewURL,
        previewWidth = previewWidth,
        previewHeight = previewHeight,
        webformatURL = webformatURL,
        webformatWidth = webformatWidth,
        webformatHeight = webformatHeight,
        largeImageURL = largeImageURL,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        imageSize = imageSize,
        views = views,
        downloads = downloads,
        favorites = favorites,
        likes = likes,
        comments = comments,
        user_id = user_id,
        user = user,
        userImageURL = userImageURL,
        page = page
    )
}

