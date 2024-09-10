package com.example.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.playlistmaker.common.INPUT_TEXT_KEY
import com.example.playlistmaker.common.PREVIEW_URL
import com.example.playlistmaker.common.RELEASE_DATE
import com.example.playlistmaker.common.SEARCH_REFRESH_RATE
import com.example.playlistmaker.common.TRACK_NAME
import com.example.playlistmaker.common.TRACK_TIME_IN_MILLIS
import com.example.playlistmaker.data.sharedPrefs.UserSharedPreferences
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.PlayerActivity


class SearchActivity : AppCompatActivity() {

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
    private lateinit var progressBar: FrameLayout
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
        progressBar = findViewById(R.id.progress_bar_layout)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyHintText = findViewById(R.id.text_hint_before_typing)

        val preferencesForTrackHistory = Creator.getPrefs(HISTORY_KEY, applicationContext)
        val sharedPreferences = Creator.provideSharedPrefs()
        searchTracksUseCase = Creator.provideTracksInteractorImpl()

        mainThreadHandler = Handler(Looper.getMainLooper())

        if (savedInstanceState != null) inputText.setText(restoredText)

        clearButton.setOnClickListener {
            inputText.text.clear()
            trackListAdapter.tracks = sharedPreferences.getHistory(preferencesForTrackHistory)
            trackListAdapter.notifyDataSetChanged()
            nothingImage.visibility = View.GONE
            connectionProblemError.visibility = View.GONE
            recycler.visibility = View.VISIBLE
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
            sharedPreferences.addHistory(preferencesForTrackHistory, sharedPreferences.historyList)
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
                    mainThreadHandler?.postDelayed(searchAction(), SEARCH_REFRESH_RATE)
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

        init(searchTextWatcher, sharedPreferences, preferencesForTrackHistory)
    }

    private fun init(
        searchTextWatcher: TextWatcher,
        sharedPreferences: UserSharedPreferences,
        preferencesForTrackHistory: SharedPreferences
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
                sharedPreferences.addHistory(
                    preferencesForTrackHistory,
                    sharedPreferences.historyList
                )
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

    private fun searchAction(): Runnable {
        return Runnable {
            historyHintText.isVisible = false
            clearHistoryButton.isVisible = false
            progressBar.isVisible = true

            searchTracksUseCase.doRequest(
                inputText.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(findTracks: List<Track>, response: Int) {
                        mainThreadHandler?.post {
                            if (findTracks.isEmpty()) {
                                if (response == 400) {
                                    mainThreadHandler?.post(connectionErrorUiElements())
                                    Log.e("resultCode", "$response")
                                    return@post
                                }
                                mainThreadHandler?.post(nothingFoundUiElements())
                                Log.e("resultCode", "$response")
                            } else {
                                mainThreadHandler?.post(successListUiElements(findTracks))
                                Log.e("resultCode", "$response")
                            }
                        }
                    }
                })
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun successListUiElements(findTracks: List<Track>) = Runnable {
        progressBar.isVisible = false
        nothingImage.visibility = View.GONE
        connectionProblemError.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        trackList.clear()
        trackListAdapter.tracks = trackList
        trackList.addAll(findTracks)
        trackListAdapter.notifyDataSetChanged()
    }

    private fun nothingFoundUiElements() = Runnable {
        progressBar.isVisible = false
        nothingImage.visibility = View.VISIBLE
        connectionProblemError.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    private fun connectionErrorUiElements() = Runnable {
        progressBar.isVisible = false
        nothingImage.visibility = View.GONE
        connectionProblemError.visibility = View.VISIBLE
        recycler.visibility = View.GONE
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
