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
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.recyclerView.Track
import com.example.playlistmaker.recyclerView.TrackAdapter
import com.example.playlistmaker.retrofit.ITunesApi
import com.example.playlistmaker.retrofit.TracksResponse
import com.example.playlistmaker.savings.SearchHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val INPUT_TEXT_KEY = "inputText"
const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {


    private val imdbBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val imdbService = retrofit.create(ITunesApi::class.java)

    private lateinit var recycler: RecyclerView
    private var trackList = ArrayList<Track>()
    private var historyList = ArrayList<Track>()

    private val searchHistoryClass = SearchHistory()
    var trackListAdapter = TrackAdapter()

    private var restoredText = ""

    private lateinit var inputText: EditText
    private lateinit var nothingImage: LinearLayout
    private lateinit var connectionProblemError: LinearLayout
    lateinit var historyHintText: TextView
    lateinit var clearHistoryButton: Button

    private val spH = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)

    @SuppressLint("NotifyDataSetChanged")
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
        inputText = findViewById(R.id.search_input_text)
        recycler = findViewById(R.id.search_list)
        nothingImage = findViewById(R.id.nothingFound)
        connectionProblemError = findViewById(R.id.connectionProblem)
        val clearButton = findViewById<ImageView>(R.id.search_clear_button)
        val reloadButton = findViewById<Button>(R.id.reload_button)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyHintText = findViewById(R.id.text_hint_before_typing)

        if (savedInstanceState != null) inputText.setText(restoredText)

        clearButton.setOnClickListener {
            inputText.text.clear()
            trackListAdapter.tracks = historyList
            trackListAdapter.notifyDataSetChanged()
            showOnlyList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
        }
        reloadButton.setOnClickListener { searchAction() }
        clearHistoryButton.setOnClickListener {
            trackListAdapter.historyList.clear()
            trackListAdapter.tracks = trackListAdapter.historyList
            trackListAdapter.notifyDataSetChanged()
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
                historyHintText.isVisible = inputText.hasFocus() && s?.isEmpty() == true
                recycler.isVisible = inputText.hasFocus() && s?.isEmpty() == true
                clearHistoryButton.isVisible = inputText.hasFocus() && s?.isEmpty() == true

            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        init(searchTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        searchHistoryClass.addHistory(spH, trackListAdapter.historyList)
//        addHistory(trackListAdapter.historyList)
    }


//    private fun addHistory(history: ArrayList<Track>) {
//        val json = Gson().toJson(history.toTypedArray())
//        getSharedPreferences(HISTORY_KEY, MODE_PRIVATE).edit().putString(HISTORY_KEY, json).apply()
//    }
//
//    private fun getHistory(): ArrayList<Track> {
//        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
//        val json = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE).getString(HISTORY_KEY, null)
//            ?: return ArrayList()
//        return Gson().fromJson(json, itemType)
//    }

    private fun init(searchTextWatcher: TextWatcher) {
        inputText.addTextChangedListener(searchTextWatcher)
        inputText.setOnFocusChangeListener { _, hasFocus ->
            historyHintText.visibility =
                if (hasFocus && inputText.text.isEmpty()) View.VISIBLE else View.GONE
        }
        recycler.layoutManager = LinearLayoutManager(this)

//        trackListAdapter.historyList = getHistory()
        trackListAdapter.historyList = searchHistoryClass.getHistory(spH)
        historyList = trackListAdapter.historyList
//        trackListAdapter.tracks = getHistory()
        trackListAdapter.tracks = searchHistoryClass.getHistory(spH)
        recycler.adapter = trackListAdapter
    }

    private fun searchAction() {
        imdbService.search(inputText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    p0: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.isSuccessful) {
                        showOnlyList()
                        trackList.clear()
                        trackListAdapter.tracks = trackList
                        val resultsResponse = response.body()?.results
                        if (resultsResponse?.isNotEmpty() == true) {
                            trackList.addAll(resultsResponse)
                            trackListAdapter.notifyDataSetChanged()
                        } else {
                            showOnlyNothingFoundError()
                        }
                    } else {
                        showOnlyConnectionError()
                    }
                }

                override fun onFailure(p0: Call<TracksResponse>, p1: Throwable) {
                    showOnlyConnectionError()
                }
            })
    }

    private fun showOnlyNothingFoundError() {
        nothingImage.visibility = View.VISIBLE
        connectionProblemError.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    private fun showOnlyConnectionError() {
        nothingImage.visibility = View.GONE
        connectionProblemError.visibility = View.VISIBLE
        recycler.visibility = View.GONE
    }

    private fun showOnlyList() {
        nothingImage.visibility = View.GONE
        connectionProblemError.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        clearHistoryButton.visibility = View.VISIBLE
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