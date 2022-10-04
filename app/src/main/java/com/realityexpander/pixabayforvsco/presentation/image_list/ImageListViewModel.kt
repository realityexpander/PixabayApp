package com.realityexpander.pixabayforvsco.presentation.image_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.pixabayforvsco.common.Constants.ItemsPerPage
import com.realityexpander.pixabayforvsco.domain.repository.PixabayRepository
import com.realityexpander.pixabayforvsco.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: PixabayRepository
) : ViewModel() {

    var state by mutableStateOf(ImageListState())

    private var searchJob: Job? = null

    init {
        //state = state.copy(isLoading = true)
        //runBlocking {delay(500)} // To show loading state
        //getPixabayImageList("", false)
    }

    fun onEvent(event: ImageListEvent) {

        when (event) {
            // Clear cache and refresh
            is ImageListEvent.OnRefresh -> {
                clearErrorMessage()
                state = state.copy(isLoading = true, maxPageLoaded = 1)

                viewModelScope.launch {
                    // Clear the cache
                    repository.clearCacheForQuery(state.searchQuery)

                    // Get the new data
                    getPixabayImages(state.searchQuery, true)
                }
            }

            // New search query
            is ImageListEvent.OnSearchQueryChanged -> {
                clearErrorMessage()
                state = state.copy(searchQuery = event.query, isLoading = true)

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    // wait for 500ms to throttle the search
                    delay(500)

                    // Get the new data
                    getPixabayImages(event.query, false)
                }
            }
        }
    }

    // Get the list of images or the local cached list
    private fun getPixabayImages(
        query: String = state.searchQuery.lowercase(),
        isFetchFromRemote: Boolean = false
    ) {
        if(query.isEmpty()) return
        val lowerCaseQuery = query.lowercase()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository
                    .getPixabayImages(isFetchFromRemote, lowerCaseQuery)
                    .collect { result ->
                        withContext(Dispatchers.Main) {
                            state = when (result) {
                                is Resource.Success -> {
                                    println("getPixabayImageList: result.data.size: ${result.data?.size}, " +
                                            "endReached: ${(result.data?.size ?: 0) >= result.totalHits}")
                                    state.copy(
                                        pixabayImageList = result.data ?: emptyList(),
                                        //page = 1,
                                        maxPageLoaded = result.data?.maxOfOrNull { it.page } ?: 0,  // get the max page number from the list of images
                                        totalHits = result.totalHits,
                                        endReached = (result.data?.size ?: 0) >= result.totalHits
                                    )
                                }
                                is Resource.Error -> state.copy(errorMessage = result.message)
                                is Resource.Loading -> state.copy(isLoading = result.isLoading)
                            }
                        }
                    }
            }
        }
    }

    private fun clearErrorMessage() {
        state = state.copy(errorMessage = null)
    }

    fun getNextPagePixabayImages(
        query: String = state.searchQuery.lowercase(),
    ) {
        if(query.isEmpty()) return
        val lowerCaseQuery = query.lowercase()

        // Move to the next page
        val nextPage = state.maxPageLoaded + 1

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                repository
                    .getNextPagePixabayImages(lowerCaseQuery, nextPage, ItemsPerPage)
                    .collect { result ->
                        withContext(Dispatchers.Main) {
                            state = when (result) {
                                is Resource.Success -> {
                                    println("loadNextItems: result.data.size: ${result.data?.size}, " +
                                            "endReached: ${(result.data?.size ?: 0) >= result.totalHits}, " +
                                            "maxPageLoaded: ${state.maxPageLoaded}, "+
                                            "nextPage: $nextPage"
                                    )

                                    state.copy(
                                        pixabayImageList = result.data ?: emptyList(),
                                        maxPageLoaded = nextPage,
                                        totalHits = result.totalHits,
                                        endReached = (result.data?.size ?: 0) >= result.totalHits
                                    )
                                }
                                is Resource.Error -> state.copy(errorMessage = result.message)
                                is Resource.Loading -> state.copy(isLoading = result.isLoading)
                            }
                        }
                    }
            }
        }
    }
}
