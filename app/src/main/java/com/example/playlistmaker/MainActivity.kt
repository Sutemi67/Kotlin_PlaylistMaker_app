package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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
      val buttonSearchOnClickListener: View.OnClickListener = object : View.OnClickListener {
         override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "За любовь!", Toast.LENGTH_SHORT).show()
         }
      }
      val buttonMedia = findViewById<Button>(R.id.button_media)
      buttonMedia.setOnClickListener {
         Toast.makeText(this@MainActivity, "Чтоб деньги были!", Toast.LENGTH_SHORT).show()
      }
      val buttonSettings = findViewById<Button>(R.id.button_settings)
      buttonSettings.setOnClickListener {
         Toast.makeText(this@MainActivity, "Живите счастливо!", Toast.LENGTH_SHORT).show()
      }
      buttonSearch.setOnClickListener(buttonSearchOnClickListener)
   }
}