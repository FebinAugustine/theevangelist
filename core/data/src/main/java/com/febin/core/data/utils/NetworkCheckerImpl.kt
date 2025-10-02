package com.febin.core.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.febin.core.domain.utils.NetworkChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NetworkCheckerImpl(private val context: Context) : NetworkChecker {
    override suspend fun hasInternetConnection(): Boolean = withContext(Dispatchers.IO) @androidx.annotation.RequiresPermission(
        android.Manifest.permission.ACCESS_NETWORK_STATE
    ) {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return@withContext false

            val activeNetwork = cm.activeNetwork ?: return@withContext false
            val nc = cm.getNetworkCapabilities(activeNetwork) ?: return@withContext false

            val hasInternet = nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val validated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else true // on older devices, assume the capability is adequate

            return@withContext hasInternet && validated
        } catch (t: Throwable) {
            // be conservative on exception — treat as no internet
            return@withContext false
        }
    }
}
