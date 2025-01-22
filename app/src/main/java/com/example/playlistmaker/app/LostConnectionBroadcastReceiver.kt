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
                // Сеть доступна
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    // делаем дела когда доступна
                } else {
                    // есть сеть, но нет интернета
                    Snackbar.make(
                        rootView,
                        "Отсутствует интернет в данной сети",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                // нет доступных сетей
                Snackbar.make(rootView, "Нет доступных сетей", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}