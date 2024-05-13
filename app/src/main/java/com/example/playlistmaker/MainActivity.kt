package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContentView(R.layout.activity_main)
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
         
      }
      val buttonSearch = findViewById<Button>(R.id.button_search)
      val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
         override fun onClick(v: View?) {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
         }
      }
      buttonSearch.setOnClickListener { buttonSearchClickListener.onClick(it) }
      
      val buttonMedia = findViewById<Button>(R.id.button_media)
      buttonMedia.setOnClickListener {
         startActivity(Intent(this, MediaActivity::class.java))
      }
      val buttonSettings = findViewById<Button>(R.id.button_settings)
      buttonSettings.setOnClickListener {
         startActivity(Intent(this, SettingsActivity::class.java))
      }
      
   }
}