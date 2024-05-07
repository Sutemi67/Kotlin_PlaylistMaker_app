package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContentView(R.layout.lesson_images)
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
      }
      val image = findViewById<ImageView>(R.id.poster)
      image.setOnClickListener{Toast.makeText(this, "You clicked the image", Toast.LENGTH_SHORT).show()}
   }
}
