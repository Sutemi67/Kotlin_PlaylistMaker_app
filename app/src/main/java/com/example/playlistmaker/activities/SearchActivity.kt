package com.example.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
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
import com.example.playlistmaker.savings.Savings
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HISTORY_KEY = "history_key"

class SearchActivity : AppCompatActivity() {
    companion object {
        const val ITUNES_URL = "https://itunes.apple.com"
        const val INPUT_TEXT_KEY = "inputText"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val imdbService = retrofit.create(ITunesApi::class.java)

    private lateinit var recycler: RecyclerView
    private var trackList = ArrayList<Track>()
    private var historyList = ArrayList<Track>()

    var trackListAdapter = TrackAdapter()

    private var restoredText = ""

    private lateinit var inputText: EditText
    private lateinit var nothingImage: LinearLayout
    private lateinit var connectionProblemError: LinearLayout
    lateinit var historyHintText: TextView
    lateinit var clearHistoryButton: Button


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

        findViewById<Toolbar>(R.id.backIcon_search_screen2).setNavigationOnClickListener { finish() }
        inputText = findViewById(R.id.search_input_text)
        recycler = findViewById(R.id.search_list)
        nothingImage = findViewById(R.id.nothingFound)
        connectionProblemError = findViewById(R.id.connectionProblem)
        val clearButton = findViewById<ImageView>(R.id.search_clear_button)
        val reloadButton = findViewById<Button>(R.id.reload_button)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyHintText = findViewById(R.id.text_hint_before_typing)

        val preferencesForTrackHistory = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        val savingsClass = Savings()


        if (savedInstanceState != null) inputText.setText(restoredText)

        clearButton.setOnClickListener {
            inputText.text.clear()
            trackListAdapter.tracks = savingsClass.getHistory(preferencesForTrackHistory)
            trackListAdapter.notifyDataSetChanged()
            showOnlyList()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
            clearHistoryButton.isVisible = trackListAdapter.tracks.isEmpty() == false
            historyHintText.isVisible = historyList.isEmpty() == false
        }
        reloadButton.setOnClickListener { searchAction() }
        clearHistoryButton.setOnClickListener {
            savingsClass.historyList.clear()
            trackListAdapter.tracks = savingsClass.historyList
            trackListAdapter.notifyDataSetChanged()
            addHistory(preferencesForTrackHistory, savingsClass.historyList)
            clearHistoryButton.isVisible = trackListAdapter.tracks.isEmpty() == false
            historyHintText.isVisible = historyList.isEmpty() == false
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
                if (historyList.isNotEmpty()) {
                    historyHintText.isVisible = inputText.hasFocus() && s?.isEmpty() == true
                    recycler.isVisible = inputText.hasFocus() && s?.isEmpty() == true
                    clearHistoryButton.isVisible = inputText.hasFocus() && s?.isEmpty() == true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        init(searchTextWatcher, savingsClass, preferencesForTrackHistory)
    }

    private fun init(
        searchTextWatcher: TextWatcher,
        savingsClass: Savings,
        preferencesForTrackHistory: SharedPreferences
    ) {
        inputText.addTextChangedListener(searchTextWatcher)
        inputText.setOnFocusChangeListener { _, hasFocus ->
            historyHintText.isVisible =
                hasFocus && inputText.text.isEmpty() && historyList.isEmpty() == false
        }
        recycler.layoutManager = LinearLayoutManager(this)

        savingsClass.historyList = savingsClass.getHistory(preferencesForTrackHistory)
        historyList = savingsClass.historyList
        trackListAdapter.tracks = historyList
        recycler.adapter = trackListAdapter
        clearHistoryButton.isVisible = trackListAdapter.tracks.isEmpty() == false
        historyHintText.isVisible = historyList.isEmpty() == false


        trackListAdapter.saveClickListener = object : TrackAdapter.SaveTrackInHistoryListener {
            override fun saveTrackInHistory() {
                Log.d("SaveTag", "Saving....")
                addHistory(preferencesForTrackHistory, savingsClass.historyList)
            }
        }
        trackListAdapter.addingInHistoryLogicListener =
            object : TrackAdapter.AddInHistoryLogicListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun savingLogic(position: Int) {

                    if (savingsClass.historyList.size < 10) {
                        if (savingsClass.historyList.isNotEmpty()) {
                            for (i in 0..<savingsClass.historyList.size) {
                                if (trackListAdapter.tracks[position].trackId == savingsClass.historyList[i].trackId) {

                                    trackListAdapter.tracks.add(
                                        0,
                                        trackListAdapter.tracks[position]
                                    )
                                    if (trackListAdapter.tracks.size < 11) {
                                        trackListAdapter.notifyItemInserted(0)
                                    }
                                    Log.d(
                                        "Adding",
                                        "добавили трек с позиции $position"
                                    )
                                    trackListAdapter.tracks.removeAt(position + 1)
                                    if (trackListAdapter.tracks.size < 11) {
                                        trackListAdapter.notifyDataSetChanged()
                                    }

                                    Log.d(
                                        "Adding",
                                        "Удален трек с индексом $position"
                                    )
                                    return
                                }
                            }
                        }
                        Log.d(
                            "Adding",
                            "Дошли до добавления трека, размер массива истории ${savingsClass.historyList.size}, треклиста ${trackListAdapter.tracks.size}"
                        )
                        savingsClass.historyList.add(0, trackListAdapter.tracks[position])
                        if (trackListAdapter.tracks.size < 11) {
                            trackListAdapter.notifyItemInserted(0)
                        }
                        Log.d(
                            "Adding",
                            "Меньше 10 треков список, добавлен трек позиции $position без повторений, размер массива истории ${historyList.size}"
                        )
                    } else {
                        for (i in 0..<savingsClass.historyList.size) {
                            if (trackListAdapter.tracks[position].trackId == savingsClass.historyList[i].trackId) {
                                trackListAdapter.tracks.add(0, trackListAdapter.tracks[position])
                                if (trackListAdapter.tracks.size < 11) {
                                    trackListAdapter.notifyItemInserted(0)
                                }
                                Log.d(
                                    "Adding",
                                    "добавили трек с позиции $position"
                                )

                                trackListAdapter.tracks.removeAt(position + 1)
                                if (trackListAdapter.tracks.size < 11) {
                                    trackListAdapter.notifyDataSetChanged()
                                }
                                Log.d(
                                    "Adding",
                                    "Удален трек с индексом $position"
                                )
                                return
                            }
                        }
                        savingsClass.historyList.removeAt(9)
                        savingsClass.historyList.add(0, trackListAdapter.tracks[position])
                        if (trackListAdapter.tracks.size < 11) {
                            trackListAdapter.notifyItemInserted(0)
                        }
                        Log.d("Adding", "добавлен вместо 10 трека")
                    }
                }
            }

        trackListAdapter.openPlayerActivity = object : TrackAdapter.OpenPlayerActivity {
            override fun openPlayerActivity(track: Track) {
                val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                intent.putExtra("trackName", track.trackName)
                intent.putExtra("artist", track.artistName)
                intent.putExtra("artworkUrl100", track.artworkUrl100)
                intent.putExtra("collectionName", track.collectionName)
                intent.putExtra("country", track.country)
                intent.putExtra("primaryGenreName", track.primaryGenreName)
                intent.putExtra("releaseDate", track.releaseDate)
                intent.putExtra("trackTimeMillis", track.trackTime)
                startActivity(intent)
            }
        }
    }


    fun addHistory(preferencesForTrackHistory: SharedPreferences, history: ArrayList<Track>) {
        val json = Gson().toJson(history.toTypedArray())
        preferencesForTrackHistory.edit().putString(HISTORY_KEY, json).apply()
    }

    private fun searchAction() {
        historyHintText.isVisible = false
        clearHistoryButton.isVisible = false

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
