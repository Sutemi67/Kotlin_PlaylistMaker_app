package com.example.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
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
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.common.ARTIST
import com.example.playlistmaker.common.ARTWORK_URL
import com.example.playlistmaker.common.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.common.COLLECTION_NAME
import com.example.playlistmaker.common.COUNTRY
import com.example.playlistmaker.common.GENRE
import com.example.playlistmaker.common.HISTORY_KEY
import com.example.playlistmaker.common.PREVIEW_URL
import com.example.playlistmaker.common.RELEASE_DATE
import com.example.playlistmaker.common.TRACK_NAME
import com.example.playlistmaker.common.TRACK_TIME_IN_MILLIS
import com.example.playlistmaker.data.sharedPrefs.UserSharedPreferences
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {
    companion object {
        const val ITUNES_URL = "https://itunes.apple.com"
        const val INPUT_TEXT_KEY = "inputText"
        const val SEARCH_REFRESH_RATE = 2000L
    }

    private var mainThreadHandler: Handler? = null
    private var isClickAllowed = true

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
    private lateinit var searchTracksUseCase: TracksInteractor

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
        val progressBarLayout = findViewById<FrameLayout>(R.id.progress_bar_layout)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyHintText = findViewById(R.id.text_hint_before_typing)


        val preferencesForTrackHistory = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        val sharedPreferences = UserSharedPreferences()
        searchTracksUseCase = Creator.provideTracksInteractorImpl()

        mainThreadHandler = Handler(Looper.getMainLooper())

        if (savedInstanceState != null) inputText.setText(restoredText)

        clearButton.setOnClickListener {
            inputText.text.clear()
            trackListAdapter.tracks = sharedPreferences.getHistory(preferencesForTrackHistory)
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
            sharedPreferences.historyList.clear()
            trackListAdapter.tracks = sharedPreferences.historyList
            trackListAdapter.notifyDataSetChanged()
            addHistory(preferencesForTrackHistory, sharedPreferences.historyList)
            clearHistoryButton.isVisible = trackListAdapter.tracks.isEmpty() == false
            historyHintText.isVisible = historyList.isEmpty() == false
        }

        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mainThreadHandler?.post(searchAction())
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
                mainThreadHandler?.postDelayed(
                    searchActionTask(progressBarLayout), SEARCH_REFRESH_RATE
                )
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        init(searchTextWatcher, sharedPreferences, preferencesForTrackHistory)
    }

    private fun init(
        searchTextWatcher: TextWatcher,
        sharedPreferences: UserSharedPreferences,
        preferencesForTrackHistory: android.content.SharedPreferences
    ) {
        inputText.addTextChangedListener(searchTextWatcher)
        inputText.setOnFocusChangeListener { _, hasFocus ->
            historyHintText.isVisible =
                hasFocus && inputText.text.isEmpty() && historyList.isEmpty() == false
        }
        recycler.layoutManager = LinearLayoutManager(this)

        sharedPreferences.historyList = sharedPreferences.getHistory(preferencesForTrackHistory)
        historyList = sharedPreferences.historyList
        trackListAdapter.tracks = historyList
        recycler.adapter = trackListAdapter
        clearHistoryButton.isVisible = trackListAdapter.tracks.isEmpty() == false
        historyHintText.isVisible = historyList.isEmpty() == false


        trackListAdapter.saveClickListener = object : TrackAdapter.SaveTrackInHistoryListener {
            override fun saveTrackInHistory() {
                Log.d("SaveTag", "Saving....")
                addHistory(preferencesForTrackHistory, sharedPreferences.historyList)
            }
        }
        trackListAdapter.addingInHistoryLogicListener =
            object : TrackAdapter.AddInHistoryLogicListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun savingLogic(position: Int) {

                    if (sharedPreferences.historyList.size < 10) {
                        if (sharedPreferences.historyList.isNotEmpty()) {
                            for (i in 0..<sharedPreferences.historyList.size) {
                                if (trackListAdapter.tracks[position].trackId == sharedPreferences.historyList[i].trackId) {

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
                            "Дошли до добавления трека, размер массива истории ${sharedPreferences.historyList.size}, треклиста ${trackListAdapter.tracks.size}"
                        )
                        sharedPreferences.historyList.add(0, trackListAdapter.tracks[position])
                        if (trackListAdapter.tracks.size < 11) {
                            trackListAdapter.notifyItemInserted(0)
                        }
                        Log.d(
                            "Adding",
                            "Меньше 10 треков список, добавлен трек позиции $position без повторений, размер массива истории ${historyList.size}"
                        )
                    } else {
                        for (i in 0..<sharedPreferences.historyList.size) {
                            if (trackListAdapter.tracks[position].trackId == sharedPreferences.historyList[i].trackId) {
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
                        sharedPreferences.historyList.removeAt(9)
                        sharedPreferences.historyList.add(0, trackListAdapter.tracks[position])
                        if (trackListAdapter.tracks.size < 11) {
                            trackListAdapter.notifyItemInserted(0)
                        }
                        Log.d("Adding", "добавлен вместо 10 трека")
                    }
                }
            }

        trackListAdapter.openPlayerActivity = object : TrackAdapter.OpenPlayerActivity {
            override fun openPlayerActivity(track: Track) {
                if (isClickAllowed) {
                    isClickAllowed = false
                    mainThreadHandler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
                    val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                    intent.putExtra(TRACK_NAME, track.trackName)
                    intent.putExtra(ARTIST, track.artistName)
                    intent.putExtra(ARTWORK_URL, track.artworkUrl100)
                    intent.putExtra(COLLECTION_NAME, track.collectionName)
                    intent.putExtra(COUNTRY, track.country)
                    intent.putExtra(GENRE, track.primaryGenreName)
                    intent.putExtra(RELEASE_DATE, track.releaseDate)
                    intent.putExtra(TRACK_TIME_IN_MILLIS, track.trackTime)
                    intent.putExtra(PREVIEW_URL, track.previewUrl)
                    startActivity(intent)
                }
            }
        }
    }


    fun addHistory(
        preferencesForTrackHistory: android.content.SharedPreferences,
        history: ArrayList<Track>
    ) {
        val json = Gson().toJson(history.toTypedArray())
        preferencesForTrackHistory.edit().putString(HISTORY_KEY, json).apply()
    }

    private fun searchAction(): Runnable {
        return Runnable {
            historyHintText.isVisible = false
            clearHistoryButton.isVisible = false

            searchTracksUseCase.doRequest(
                inputText.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(findTracks: List<Track>) {
                        if (findTracks.isEmpty()) {
                            mainThreadHandler?.post(nothingFoundRunnable)
//                            showOnlyNothingFoundError()
                        } else {
//                            showOnlyList()
                            mainThreadHandler?.post(onlyListRunnable)
                            mainThreadHandler?.post(updateTrackList(findTracks))
                        }
                    }
                })
        }
//        imdbService.search(inputText.text.toString())
//            .enqueue(
//                object : Callback<TracksResponse> {
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onResponse(
//                        p0: Call<TracksResponse>,
//                        response: Response<TracksResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            showOnlyList()
//                            trackList.clear()
//                            trackListAdapter.tracks = trackList
//                            val resultsResponse = response.body()?.results
//                            if (resultsResponse?.isNotEmpty() == true) {
//                                trackList.addAll(resultsResponse)
//                                trackListAdapter.notifyDataSetChanged()
//                            } else {
//                                showOnlyNothingFoundError()
//                            }
//                        } else {
//                            showOnlyConnectionError()
//                        }
//                    }
//
//                    override fun onFailure(p0: Call<TracksResponse>, p1: Throwable) {
//                        showOnlyConnectionError()
//                    }
//                })
    }

    private fun updateTrackList(findTracks: List<Track>): Runnable {
        return Runnable {
            trackList.clear()
            trackListAdapter.tracks = trackList
            trackList.addAll(findTracks)
            trackListAdapter.notifyDataSetChanged()
        }
    }

    private fun searchActionTask(progressBar: FrameLayout): Runnable {
        return Runnable {
            historyHintText.isVisible = false
            clearHistoryButton.isVisible = false
            progressBar.isVisible = true

//            imdbService.search(inputText.text.toString())
//                .enqueue(object : Callback<TracksResponse> {
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onResponse(
//                        p0: Call<TracksResponse>,
//                        response: Response<TracksResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            showOnlyList()
//                            trackList.clear()
//                            trackListAdapter.tracks = trackList
//                            progressBar.isVisible = false
//                            val resultsResponse = response.body()?.results
//                            if (resultsResponse?.isNotEmpty() == true) {
//                                trackList.addAll(resultsResponse)
//                                trackListAdapter.notifyDataSetChanged()
//                            } else {
//                                showOnlyNothingFoundError()
//                                progressBar.isVisible = false
//                            }
//                        } else {
//                            showOnlyConnectionError()
//                            progressBar.isVisible = false
//                        }
//                    }
//
//                    override fun onFailure(p0: Call<TracksResponse>, p1: Throwable) {
//                        showOnlyConnectionError()
//                        progressBar.isVisible = false
//                    }
//                })
        }
    }

    private val nothingFoundRunnable = Runnable { showOnlyNothingFoundError() }
    private val onlyListRunnable = Runnable { showOnlyList() }

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
