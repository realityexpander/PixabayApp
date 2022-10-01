package com.realityexpander.pixabayforvsco.data.repository

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}