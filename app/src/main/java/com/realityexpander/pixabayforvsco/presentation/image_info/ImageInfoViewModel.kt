package com.realityexpander.pixabayforvsco.presentation.image_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.pixabayforvsco.domain.repository.PixabayRepository
import com.realityexpander.pixabayforvsco.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,  // contains the navArgs
    private val repository: PixabayRepository
) : ViewModel() {

    //change to StateFlow instead of compose State
    var state by mutableStateOf(ImageInfoState())

    init {

        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            state = state.copy(isLoading = true)

            val pixabayImages = async {
                repository.getPixabayImage(id)
            }

            state = when (val result = pixabayImages.await()) {
                is Resource.Success -> {
                        state.copy(
                            isLoading = false,
                            pixabayImage = result.data
                        )
                }
                is Resource.Error -> {
                    state.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        pixabayImage = null
                    )
                }
                is Resource.Loading -> state // do nothing
            }

        }
    }

}
