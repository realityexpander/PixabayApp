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

    private fun getPixabayImageList(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository
                    .getPixabayImages(fetchFromRemote, query)
                    .collect { result ->
                        withContext(Dispatchers.Main) {
                            state = when (result) {
                                is Resource.Success -> state.copy(
                                    pixabayImageList = result.data ?: emptyList()
                                )
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
}
