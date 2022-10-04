package com.realityexpander.pixabayforvsco.data.remote

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    fun getNetworkStatusDeprecated(): Status

    fun getNetworkStatus(): Status

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}