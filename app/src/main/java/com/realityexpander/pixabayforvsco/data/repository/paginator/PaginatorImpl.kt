package com.realityexpander.pixabayforvsco.data.repository.paginator

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