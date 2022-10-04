package com.realityexpander.pixabayforvsco.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged


@OptIn(ExperimentalCoroutinesApi::class)
class ConnectivityObserverImpl(
    private val context: Context
): ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // use connectivityManager.requestNetwork(networkRequest, networkCallback) for api levels lower than 24

    @RequiresApi(Build.VERSION_CODES.N)
    override fun observe(): Flow<ConnectivityObserver.Status> {

        return callbackFlow {

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(ConnectivityObserver.Status.Available)
                    //launch { send(ConnectivityObserver.Status.Available) } // can also use `offer`
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    trySend(ConnectivityObserver.Status.Losing)
                    //launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(ConnectivityObserver.Status.Lost)
                    //launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(ConnectivityObserver.Status.Unavailable)
                    //launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }

            // Start listening for network changes
            connectivityManager.registerDefaultNetworkCallback(callback)

            // closes only when the scope that launched it is cancelled (e.g. when the activity/fragment is destroyed)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    // old way
    override fun isNetworkAvailableDeprecated(): ConnectivityObserver.Status {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return if (activeNetwork != null && activeNetwork.isConnected) {
            ConnectivityObserver.Status.Available
        } else {
            ConnectivityObserver.Status.Unavailable
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun getNetworkStatus(): ConnectivityObserver.Status {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val capabilities = manager
            ?.getNetworkCapabilities(manager.activeNetwork) ?: return ConnectivityObserver.Status.Unavailable

        return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) )
                ConnectivityObserver.Status.Available
            else
                ConnectivityObserver.Status.Unavailable
    }
}