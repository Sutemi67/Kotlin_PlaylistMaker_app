package com.example.playlistmaker.app

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.playlistmaker.player.ui.PlayerViewModel

class MusicServiceConnection(private val viewModel: PlayerViewModel) : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as PlayerService.PlayerServiceBinder
        viewModel.setAudioPlayerControl(binder.getService())
        Log.i("MusicService", "Service connected")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        viewModel.removeAudioPlayerControl()
        Log.e("MusicService", "service disconnected")

    }
}