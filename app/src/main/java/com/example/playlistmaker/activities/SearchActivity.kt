package com.example.playlistmaker.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.recyclerView.Track
import com.example.playlistmaker.recyclerView.TrackAdapter
import com.example.playlistmaker.retrofit.ITunesApi
import com.example.playlistmaker.retrofit.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val imdbBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val imdbService = retrofit.create(ITunesApi::class.java)

    val adapter = TrackAdapter()
    private val trackList = ArrayList<Track>()

    private var restoredText = ""

    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.backIcon_search_screen).setOnClickListener { finish() }

        val inputText = findViewById<EditText>(R.id.search_input_text)
        recycler = findViewById(R.id.search_list)
        val nothingImage = findViewById<LinearLayout>(R.id.nothingFound)
        val connectionProblemError = findViewById<LinearLayout>(R.id.connectionProblem)
        val clearButton = findViewById<ImageView>(R.id.search_clear_button)

        if (savedInstanceState != null) inputText.setText(restoredText)


        clearButton.setOnClickListener {
            inputText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
        }

        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imdbService.search(inputText.text.toString())
                    .enqueue(object : Callback<TracksResponse> {
                        override fun onResponse(
                            p0: Call<TracksResponse>,
                            response: Response<TracksResponse>
                        ) {
                            if (response.code() == 200) {
                                nothingImage.visibility = View.GONE
                                connectionProblemError.visibility = View.GONE
                                trackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                } else {
                                    nothingImage.visibility = View.VISIBLE
                                }
                            } else {
                                connectionProblemError.visibility = View.VISIBLE
                            }
                        }

                        override fun onFailure(p0: Call<TracksResponse>, p1: Throwable) {
                            Toast.makeText(applicationContext, "Нет ответа", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
            false
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

        recycler.layoutManager = LinearLayoutManager(this)
        adapter.tracks = trackList
        recycler.adapter = adapter
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