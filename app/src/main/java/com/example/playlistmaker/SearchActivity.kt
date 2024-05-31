package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    var inputText: EditText = findViewById(R.id.search_input_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        if(savedInstanceState!=null){
//            inputText = savedInstanceState.getString("inputText", "")
//        }
        val backButton = findViewById<ImageView>(R.id.backIcon_search_screen)
        backButton.setOnClickListener {
            finish()
        }
        //val inputText = findViewById<EditText>(R.id.search_input_text)

        val clearButton = findViewById<ImageView>(R.id.search_clear_button)
        clearButton.setOnClickListener {
            inputText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.INVISIBLE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
                val getText = inputText.text
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        inputText.addTextChangedListener(searchTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("inputText", inputText.text.toString())
        super.onSaveInstanceState(outState)
    }
}