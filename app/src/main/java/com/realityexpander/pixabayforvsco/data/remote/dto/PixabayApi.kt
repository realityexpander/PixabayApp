package com.realityexpander.pixabayforvsco.data.remote.dto

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PixabayApi {

    @Headers("Content-Type: application/json")
    @GET ("/api/")
    suspend fun getImages(
        @Query("key") key: String = API_KEY,
        @Query("safesearch") safeSearch: Boolean = true,
        @Query("q") query: String = "",
    ): Response<PixabayResponseDTO>

    companion object {
        const val BASE_URL = "https://pixabay.com"
        const val API_KEY = "30067047-c032338bed1fa95130ce177c6"
    }
}
