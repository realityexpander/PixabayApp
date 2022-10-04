package com.realityexpander.pixabayforvsco.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val totalHits: Int = 0,  // from Pixabay API
) {
    class Success<T>(data: T?, totalHits: Int = 0) : Resource<T>(data, totalHits = totalHits)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean) : Resource<T>(null)
}
