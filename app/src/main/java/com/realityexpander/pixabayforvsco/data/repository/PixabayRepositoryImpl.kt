package com.realityexpander.pixabayforvsco.data.repository

import com.realityexpander.pixabayforvsco.data.local.PixabayDatabase
import com.realityexpander.pixabayforvsco.data.mapper.toPixabayImage
import com.realityexpander.pixabayforvsco.data.mapper.toPixabayImageEntity
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayApi
import com.realityexpander.pixabayforvsco.data.remote.dto.PixabayImage
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
    private val db: PixabayDatabase,
): PixabayRepository {

    private val dao = db.dao

    override suspend fun getPixabayImages(
        fetchFromRemote: Boolean,
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

            // Attempt to load from local cache.
            val localListings = dao.searchImagesByTag(query)
            emit(Resource.Success(
                data = localListings.map { it.toPixabayImage() },
            ))

            // Check if cache is empty.
            val isDbEmpty = localListings.isEmpty()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
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

                // Get listings from local cache, yes this is tiny bit inefficient but conforms to SSOT
                emit(Resource.Success(
                    data = dao
                        .searchImagesByOriginalSearchTerm(query)
                        .map { it.toPixabayImage() }
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
}



























