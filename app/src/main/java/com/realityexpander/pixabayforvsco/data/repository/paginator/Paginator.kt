package com.realityexpander.pixabayforvsco.data.repository.paginator

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}