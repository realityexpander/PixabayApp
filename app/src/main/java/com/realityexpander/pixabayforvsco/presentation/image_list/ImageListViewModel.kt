package com.realityexpander.pixabayforvsco.presentation.image_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                getPixabayImageList(state.searchQuery, true)
            }

            is ImageListEvent.OnSearchQueryChanged -> {
                clearErrorMessage()
                state = state.copy(searchQuery = event.query, isLoading = true)

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500) // wait for 500ms to throttle the search
                    getPixabayImageList(event.query, false)
                }
            }
        }
    }

    // Get the initial list of image (first page) or the local cached list
    private fun getPixabayImageList(
        query: String = state.searchQuery.lowercase(),
        isFetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository
                    .getPixabayImages(isFetchFromRemote, query)
                    .collect { result ->
                        withContext(Dispatchers.Main) {
                            state = when (result) {
                                is Resource.Success -> {
                                    println("getPixabayImageList: result.data.size: ${result.data?.size}, endReached: ${(result.data?.size ?: 0) >= result.totalHits}")
                                    state.copy(
                                        pixabayImageList = result.data ?: emptyList(),
                                        page = result.maxCachedPage, //result.data?.maxOfOrNull { it.page } ?: 0,  // get the max page number of the list
                                        totalHits = result.totalHits,
                                        maxCachedPage = result.maxCachedPage,
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

    fun getNextPixabayImagePageList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getNextPagePixabayImages(state.searchQuery, state.page + 1)
                    .collect { result ->
                        withContext(Dispatchers.Main) {
                            state = when (result) {
                                is Resource.Success -> {
                                    println("loadNextItems: result.data.size: ${result.data?.size}, endReached: ${(result?.data?.size ?: 0) >= result.totalHits}")
                                    state.copy(
                                        pixabayImageList = result.data ?: emptyList(),
                                        page = state.page + 1,
                                        totalHits = result.totalHits,
                                        maxCachedPage = state.page + 1,
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
