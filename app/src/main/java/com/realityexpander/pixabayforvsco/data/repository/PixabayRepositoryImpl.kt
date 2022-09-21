package com.realityexpander.pixabayforvsco.data.repository

import com.realityexpander.pixabayforvsco.data.local.StockDatabase
import com.realityexpander.pixabayforvsco.data.mapper.toPixabayImage
import com.realityexpander.pixabayforvsco.data.mapper.toPixabayImageEntity
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayApi
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayImage
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayResponse
import com.realityexpander.pixabayforvsco.domain.repository.PixabayRepository
import com.realityexpander.pixabayforvsco.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PixabayRepositoryImpl @Inject constructor(
    private val api: PixabayApi,
    private val db: StockDatabase,
): PixabayRepository {

    private val dao = db.dao

    suspend fun getPixabayImagesCached(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<PixabayImage>>> {
        return flow {
            emit(Resource.Loading(true))

            // Attempt to load from local cache.
            val localListings = dao.searchPixabay(query)
            emit(Resource.Success(
                data = localListings.map { it.toPixabayImage() },
            ))

            // Check if cache is empty.
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false )) // Cache is good, We're done here.
                return@flow
            }

            // Attempt to load from remote.
            val remoteImages = try {
                api.getImages(query)
            } catch (e: IOException) { // parse error
                e.printStackTrace()
                emit(Resource.Error(e.localizedMessage ?: "Error loading or parsing image listings"))
                null
            } catch (e: HttpException) { // invalid network response
                e.printStackTrace()
                emit(Resource.Error(e.localizedMessage ?: "Error with network for image listings"))
                null
            } catch (e: Exception) { // other error
                e.printStackTrace()
                emit(Resource.Error("$e" ?: "Unknown Error loading or parsing image listings"))
                null
            }

            // Save to local cache.
            remoteImages?.let { images ->
                dao.clearPixabayImages()
                images.body()?.hits?.map { it.toPixabayImageEntity() }?.let {
                    dao.insertPixabayImages(
                        it
                    )
                }

                // Get listings from local cache, yes this is tiny bit inefficient but conforms to SSOT
                emit(Resource.Success(
                    data = dao
                        //.searchCompanyListing("")
                        .searchPixabay("")
                        .map { it.toPixabayImage() }
                ))
            }
            emit(Resource.Loading(false))
        }
    }


//    override suspend fun getIntradayInfos(stockSymbol: String): Resource<List<IntradayInfo>> {
//        return try {
//            val response = api.getIntradayInfo(stockSymbol).byteStream()
//            // println(response.readBytes().toString(Charsets.UTF_8)) // keep for debugging
//
//            val results = intradayInfoCSVParser.parse(response)
//            Resource.Success(results)
//        } catch (e: IOException) { // parse error
//            e.printStackTrace()
//            Resource.Error(e.localizedMessage ?: "Error loading or parsing intraday info")
//        } catch (e: HttpException) { // invalid network response
//            e.printStackTrace()
//            Resource.Error(e.localizedMessage ?: "Error with network for intraday info")
//        } catch (e: Exception) { // unknown error
//            e.printStackTrace()
//            Resource.Error("$e" ?: "Unknown Error loading or parsing intraday info")
//        }
//    }
//
    override suspend fun getPixabayImages(searchString: String): Resource<PixabayResponse> {
        return try {
            val response = api.getImages(searchString)

            // Check for API limit hit
            if (!response.isSuccessful) { // call doesn't fail, just returns null data.
                Resource.Error("API limit reached, please try again later.")
            } else {
                Resource.Success(response.body())
            }
        } catch (e: IOException) { // parse error
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error loading or parsing image info")
        } catch (e: HttpException) { // invalid network response
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Error with network for image info")
        } catch (e: Exception) { // unknown error
            e.printStackTrace()
            Resource.Error("$e" ?: "Unknown Error loading or parsing image info")
        }
    }
}



























