package cl.duoc.levelupapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: return false
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    val hasTransport = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) ||
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)

    if (!hasTransport) return false

    val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    val isValidated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    } else {
        true
    }

    return hasInternet && isValidated
}
