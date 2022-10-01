package com.realityexpander.pixabayforvsco.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val totalHits: Int = 0,
    val maxCachedPage: Int = 0
) {
    class Success<T>(data: T?, totalHits: Int = 0, maxCachedPage: Int = 1) : Resource<T>(data, totalHits = totalHits, maxCachedPage = maxCachedPage)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean) : Resource<T>(null)
}
