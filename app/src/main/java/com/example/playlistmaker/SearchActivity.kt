package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class SearchActivity : AppCompatActivity() {

    private var restoredText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageView>(R.id.backIcon_search_screen)
        backButton.setOnClickListener {
            finish()
        }

        val inputText = findViewById<EditText>(R.id.search_input_text)
        if (savedInstanceState != null) {
            inputText.setText(restoredText)
        }

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
                    restoredText = inputText.text.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        inputText.addTextChangedListener(searchTextWatcher)

        val recycler = findViewById<RecyclerView>(R.id.search_list)
        val tracks = listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            )
        )
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TrackAdapter(tracks)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("inputText", restoredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoredText = savedInstanceState.getString("inputText").toString()
    }
}