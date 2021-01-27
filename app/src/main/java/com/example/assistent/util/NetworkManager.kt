package com.example.assistent.util

import android.content.Context
import android.net.ConnectivityManager

fun isOnline(context: Context):Boolean{
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    return false
}