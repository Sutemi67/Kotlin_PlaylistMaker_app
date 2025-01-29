package com.example.playlistmaker.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import com.google.android.material.snackbar.Snackbar

internal class LostConnectionBroadcastReceiver(
    private val rootView: View
) : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "android.net.conn.CONNECTIVITY_CHANGE") {
            val connectivityManager =
                p0?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (networkCapabilities != null) {
                //
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    //
                } else {
                    Snackbar.make(
                        rootView,
                        "Пропал интернет, проверьте интернет-соединение",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Snackbar.make(
                    rootView,
                    "Устройство не подключено к сети",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}