package com.example.playlistmaker.app

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PlayerService : Service() {


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}