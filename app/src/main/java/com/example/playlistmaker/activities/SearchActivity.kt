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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.recyclerView.Track
import com.example.playlistmaker.recyclerView.TrackAdapter
import com.example.playlistmaker.retrofit.ITunesApi
import com.example.playlistmaker.retrofit.TracksResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val INPUT_TEXT_KEY = "inputText"
const val HISTORY_KEY = "history_key"
const val ARRAY = "array"

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
//    private var array = Array<Track?>(10) { null }
    val trackListAdapter = TrackAdapter()
    private val historyClass = SearchHistory()
    private var restoredText = ""

    private lateinit var inputText: EditText
    private lateinit var nothingImage: LinearLayout

    private lateinit var connectionProblemError: LinearLayout
    lateinit var historyHintText: TextView


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
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
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
        clearHistoryButton.setOnClickListener { trackListAdapter.historyList.clear() }
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
                historyHintText.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                clearHistoryButton.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                recycler.visibility =
                    if (inputText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        init(searchTextWatcher)
//        historyList = historyRecovery()
    }

    override fun onStop() {
        super.onStop()
        saveHistoryFun()
    }


    private fun saveHistoryFun() {
        val sharedPref = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        val jsonHistory = Gson().toJson(trackListAdapter.historyList)
        sharedPref.edit().putString(HISTORY_KEY, jsonHistory).apply()
    }

//    private fun historyRecovery(): Array<Track> {
//        val sharedPref = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
//        val json = sharedPref.getString(HISTORY_KEY, emptyArray<Track>().toString())
//        return Gson().fromJson(json, Array<Track>::class.java)
//    }

//    fun add() {
//        historyClass.saveList(getSharedPreferences(ARRAY, MODE_PRIVATE), historyClass.array)
//    }
//
//    fun get() {
//        array = historyClass.getList(getSharedPreferences(ARRAY, MODE_PRIVATE))!!
//    }


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

    private fun init(searchTextWatcher: TextWatcher) {
        inputText.addTextChangedListener(searchTextWatcher)
        inputText.setOnFocusChangeListener { _, hasFocus ->
            historyHintText.visibility =
                if (hasFocus && inputText.text.isEmpty()) View.VISIBLE else View.GONE
        }
        recycler.layoutManager = LinearLayoutManager(this)
        trackListAdapter.tracks = trackList
        historyList = trackListAdapter.historyList
        recycler.adapter = trackListAdapter
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