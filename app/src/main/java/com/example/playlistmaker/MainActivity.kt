package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContentView(R.layout.lesson_images)
      setContentView(R.layout.activity_main)
      
      val image = findViewById<ImageView>(R.id.poster)
      
      val imageClickListener: View.OnClickListener = object : View.OnClickListener {
         override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Нажали на картинку!", Toast.LENGTH_SHORT).show()
         }
      }
      
      image.setOnClickListener(imageClickListener)
   }
}