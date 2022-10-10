package com.realityexpander.pixabayforvsco.data.repository.paginator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PaginatorImpl<Key, Item>(
    private val initialKey: Key,
    private inline val setIsLoading: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isRequestInProgress = false

    override suspend fun loadNextItems() {
        // Return if there is already a request in progress
        if(isRequestInProgress) {
            return
        }

        isRequestInProgress = true
        setIsLoading(true)

        // Call the api/database
        val result = onRequest(currentKey)
        isRequestInProgress = false
        val items = result.getOrElse {
            onError(it)
            setIsLoading(false)

            return
        }

        // Get the key for the next page
        // (Change this for pagination key, instead of using the number of items)
        // ie: currentKey = getNextKey(result.nextPageKey)
        currentKey = getNextKey(items)

        // Success
        onSuccess(items, currentKey)
        setIsLoading(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}

// Sample Viewmodel for Paginator

//class MainViewModel: ViewModel() {
//
//    private val repository = Repository()
//
//    var state by mutableStateOf(ScreenState())
//
//    private val paginator = PaginatorImpl(
//        initialKey = state.page,
//        setIsLoading = {
//            state = state.copy(isLoading = it, errorMessage = null)
//        },
//        onRequest = { nextPage ->
//            repository.getItems(nextPage, 20)
//        },
//        getNextKey = {
//            state.page + 1
//        },
//        onError = {
//            state = state.copy(errorMessage = it?.localizedMessage)
//        },
//        onSuccess = { items, newKey ->
//            state = state.copy(
//                items = state.items + items,
//                page = newKey,
//                endReached = items.isEmpty()
//            )
//        }
//    )
//
//    init {
//        loadNextItems()
//    }
//
//    fun loadNextItems() {
//        viewModelScope.launch {
//            paginator.loadNextItems()
//        }
//    }
//}
//
//data class ScreenState(
//    val isLoading: Boolean = false,
//    val items: List<ListItem> = emptyList(),
//    val errorMessage: String? = null,
//    val endReached: Boolean = false,
//    val page: Int = 0
//)