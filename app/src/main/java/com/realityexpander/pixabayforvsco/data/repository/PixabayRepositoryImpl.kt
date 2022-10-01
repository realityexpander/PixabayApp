package com.realityexpander.pixabayforvsco.data.repository

import com.realityexpander.pixabayforvsco.data.local.PixabayDatabase
import com.realityexpander.pixabayforvsco.data.mapper.toPixabayImage
import com.realityexpander.pixabayforvsco.data.mapper.toPixabayImageEntity
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayApi
import com.realityexpander.pixabayforvsco.domain.model.PixabayImage
import com.realityexpander.pixabayforvsco.domain.repository.PixabayRepository
import com.realityexpander.pixabayforvsco.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PixabayRepositoryImpl @Inject constructor(
    private val api: PixabayApi,
    private val db: PixabayDatabase,
): PixabayRepository {

    private val dao = db.dao

    override suspend fun getPixabayImages(
        isFetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<PixabayImage>>> {
        return flow {
            emit(Resource.Loading(true))

            if (query.isBlank()) {
                // If the query is blank, we don't want to fetch from remote.
                emit(Resource.Success(emptyList()))
                emit(Resource.Loading(false )) // We're done loading.
                return@flow
            }

            // Clear the cache if we're fetching from remote.
            if(isFetchFromRemote) {
                dao.clearPixabayImages()

                // Show the empty result
                emit(Resource.Success(
                    data = dao
                        .searchImagesByOriginalSearchTerm(query)
                        .map {
                            it.toPixabayImage()
                        },
                ))
            }

            // Attempt to load from local cache & show current cache results, then attempt to fetch new data from remote.
            val localPixabayImages = dao.searchImagesByOriginalSearchTerm(query)
            emit(Resource.Success(
                data = localPixabayImages.map {
                    it.toPixabayImage()
                },
                totalHits = 1000000, // TODO: Get the total hits from the API.
                maxCachedPage = localPixabayImages.maxByOrNull { it.page }?.page ?: 1
            ))

            // Check if cache is empty.
            val isDbEmpty = localPixabayImages.isEmpty()
            val isLoadOnlyFromCache = !isDbEmpty && !isFetchFromRemote
            if(isLoadOnlyFromCache) {
                emit(Resource.Loading(false )) // Cache is good, We're done here.
                return@flow
            }

            // Attempt to load from remote.
            val remoteImages = try {
                api.getImages(query = query)
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

                // Insert images into local cache.
                images
                    .body()
                    ?.hits
                    ?.map {
                        it.toPixabayImageEntity(query) // save the list with the query as "originalSearchTerm"
                    }?.let {
                        dao.insertPixabayImages(
                            it
                        )
                    }

                // Get images from local cache, yes this is tiny bit inefficient but conforms to SSOT
                emit(Resource.Success(
                    data = dao
                        .searchImagesByOriginalSearchTerm(query)
                        .map {
                            it.toPixabayImage()
                        },
                    totalHits = images.body()?.totalHits ?: 0,
                    maxCachedPage = (images.body()?.hits?.maxByOrNull { it.page }?.page) ?: 1
                ))
            }

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getPixabayImage(id: String): Resource<PixabayImage> {
        return try {
            val image = dao.getPixabayImage(id)

            // Check for API limit hit
            if (image == null) { // call doesn't fail, just returns null data.
                Resource.Error("API limit reached, please try again later.")
            } else {
                Resource.Success(image.toPixabayImage())
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

    override suspend fun getNextPagePixabayImages(
        query: String,
        page: Int,
        perPage: Int
    ): Flow<Resource<List<PixabayImage>>> {
        return flow {
            emit(Resource.Loading(true))

            if (query.isBlank()) {
                // If the query is blank, we don't want to fetch from remote.
                emit(Resource.Success(emptyList()))
                emit(Resource.Loading(false )) // We're done loading.
                return@flow
            }

            delay(1000)

            // Attempt to load next page from remote.
            val remoteImages = try {
                api.getPageOfImages(query = query, page = page, perPage = perPage)
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
                images
                    .body()
                    ?.hits
                    ?.map {
                        it.toPixabayImageEntity(query, page) // save the list with the query as "originalSearchTerm"
                    }?.let {
                        dao.insertPixabayImages(
                            it
                        )
                    }

                // Get images from local cache, yes this is tiny bit inefficient but conforms to SSOT
                emit(Resource.Success(
                    data = dao
                        .searchImagesByOriginalSearchTerm(query)
                        .map { it.toPixabayImage() },
                    totalHits = images.body()?.totalHits ?: 0
                ))
            }
            emit(Resource.Loading(false))
        }
    }
}



























