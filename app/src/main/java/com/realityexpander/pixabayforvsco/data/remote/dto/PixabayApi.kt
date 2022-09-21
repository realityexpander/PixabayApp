package com.realityexpander.pixabayforvsco.data.remote.dto

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PixabayApi {

//    // returns a csv of company listings (only csv is supported)
//    @GET("query?function=LISTING_STATUS")
//    suspend fun getListOfStocks(
//        @Query("apikey") apiKey: String = API_KEY,
//    ): ResponseBody
//
//    // returns a csv of intraday stock prices
//    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
//    suspend fun getIntradayInfo(
//        @Query("symbol") symbol: String,
//        @Query("apikey") apiKey: String = API_KEY,
//    ): ResponseBody
//
//    // returns a json of company info
//    @GET("query?function=OVERVIEW&datatype=json")
//    suspend fun getCompanyInfo(
//        @Query("symbol") symbol: String,
//        @Query("apikey") apiKey: String = API_KEY,
//    ): CompanyInfoDTO

    @Headers("Content-Type: application/json")
    @GET ("/api/")
    suspend fun getImages(
        @Query("key") key: String = API_KEY,
        @Query("safesearch") safeSearch: Boolean = true
    ): Response<PixabayResponse>


    companion object {
        const val BASE_URL = "https://pixabay.com"
        const val API_KEY = "30067047-c032338bed1fa95130ce177c6"
    }
}

data class PixabayResponse(
    val total: Int = 0,
    val totalHits: Int = 0,
    val hits: List<PixabayImage> = emptyList()
)

data class PixabayImage(
    val id: Int = 0,
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
    val userImageURL: String = ""
)