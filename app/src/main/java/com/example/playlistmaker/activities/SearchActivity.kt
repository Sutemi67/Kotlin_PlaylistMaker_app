package com.example.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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

const val INPUT_TEXT_KEY = "inputText"

class SearchActivity : AppCompatActivity() {

    private val imdbBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val imdbService = retrofit.create(ITunesApi::class.java)
    private val trackList = ArrayList<Track>()
    private var restoredText = ""
    private lateinit var recycler: RecyclerView
    val adapter = TrackAdapter()

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
        val reloadButton = findViewById<Button>(R.id.reload_button)

        if (savedInstanceState != null) inputText.setText(restoredText)

        fun searchAction() {
            imdbService.search(inputText.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        p0: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.isSuccessful) {

                            nothingImage.visibility = View.GONE
                            connectionProblemError.visibility = View.GONE
                            recycler.visibility = View.VISIBLE

                            trackList.clear()
                            val resultsResponse = response.body()?.results
                            if (resultsResponse?.isNotEmpty() == true) {
                                trackList.addAll(resultsResponse)
                                adapter.notifyDataSetChanged()
                            } else {
                                nothingImage.visibility = View.VISIBLE
                                connectionProblemError.visibility = View.GONE
                                recycler.visibility = View.GONE
                            }
                        } else {
                            nothingImage.visibility = View.GONE
                            connectionProblemError.visibility = View.VISIBLE
                            recycler.visibility = View.GONE
                        }
                    }

                    override fun onFailure(p0: Call<TracksResponse>, p1: Throwable) {
                        nothingImage.visibility = View.GONE
                        connectionProblemError.visibility = View.VISIBLE
                        recycler.visibility = View.GONE
                    }
                })
        }

        clearButton.setOnClickListener {
            inputText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
        }
        reloadButton.setOnClickListener {
            searchAction()
        }
        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchAction()
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
        outState.putString(INPUT_TEXT_KEY, restoredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoredText = savedInstanceState.getString(INPUT_TEXT_KEY).toString()
    }
}