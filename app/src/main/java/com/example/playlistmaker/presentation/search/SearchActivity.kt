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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.example.playlistmaker.common.INPUT_TEXT_KEY
import com.example.playlistmaker.common.PREVIEW_URL
import com.example.playlistmaker.common.RELEASE_DATE
import com.example.playlistmaker.common.SEARCH_REFRESH_RATE
import com.example.playlistmaker.common.TRACK_NAME
import com.example.playlistmaker.common.TRACK_TIME_IN_MILLIS
import com.example.playlistmaker.data.sharedPrefs.UserSharedPreferences
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.PlayerActivity


class SearchActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = null
    private var isClickAllowed = true
    private var isSearchAllowed = true

    private lateinit var recycler: RecyclerView
    private lateinit var binding: ActivitySearchBinding

    var trackListAdapter = Creator.provideAdapter()

    private var restoredText = ""

    private lateinit var nothingImage: LinearLayout
    private lateinit var connectionProblemError: LinearLayout
    private lateinit var progressBar: FrameLayout

    lateinit var clearHistoryButton: Button
    private lateinit var searchTracksUseCase: TracksInteractor

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backIconSearchScreen2.setNavigationOnClickListener { finish() }
        recycler = findViewById(R.id.search_list)
        nothingImage = findViewById(R.id.nothingFound)
        connectionProblemError = findViewById(R.id.connectionProblem)
        val clearButton = findViewById<ImageView>(R.id.search_clear_button)
        val reloadButton = findViewById<Button>(R.id.reload_button)
        progressBar = findViewById(R.id.progress_bar_layout)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        val sharedPreferences = Creator.provideSharedPrefs(this)
        searchTracksUseCase = Creator.provideTracksInteractorImpl()

        mainThreadHandler = Handler(Looper.getMainLooper())

        if (savedInstanceState != null) binding.searchInputText.setText(restoredText)

        clearButton.setOnClickListener {
            binding.searchInputText.text.clear()
            trackListAdapter.setData(sharedPreferences.getHistory())
            nothingImage.isVisible = false
            connectionProblemError.isVisible = false
            recycler.isVisible = true
            binding.historyLayout.isVisible = true
            binding.clearHistoryButton.isVisible = false
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
            clearHistoryButton.isVisible = trackListAdapter.getTrackList().isEmpty() == false
            binding.textHintBeforeTyping.isVisible =
                sharedPreferences.getHistory().isEmpty() == false
        }
        reloadButton.setOnClickListener { mainThreadHandler?.post(searchAction()) }
        clearHistoryButton.setOnClickListener {
            sharedPreferences.clearHistory()
            trackListAdapter.setData(emptyList())
            clearHistoryButton.isVisible = false
            binding.historyLayout.isVisible = true
            binding.textHintBeforeTyping.isVisible = false
        }
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
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
                    clearButton.isVisible = false
                } else {
                    clearButton.isVisible = true
                    restoredText = binding.searchInputText.text.toString()
                    if (isSearchAllowed) {
                        isSearchAllowed = false
                        mainThreadHandler?.postDelayed(searchAction(), SEARCH_REFRESH_RATE)
                        mainThreadHandler?.postDelayed(
                            { isSearchAllowed = true },
                            SEARCH_REFRESH_RATE
                        )
                    }
                }
                if (sharedPreferences.getHistory().isNotEmpty()) {
                    binding.textHintBeforeTyping.isVisible =
                        binding.searchInputText.hasFocus() && s?.isEmpty() == true
                    binding.historyLayout.isVisible =
                        binding.searchInputText.hasFocus() && s?.isEmpty() == true
                    clearHistoryButton.isVisible =
                        binding.searchInputText.hasFocus() && s?.isEmpty() == true

                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        init(searchTextWatcher, sharedPreferences)
    }

    private fun init(
        searchTextWatcher: TextWatcher,
        sharedPreferences: UserSharedPreferences
    ) {
        trackListAdapter.setData(sharedPreferences.getHistory())
        recycler.isVisible = true
        binding.historyLayout.isVisible = true
        binding.textHintBeforeTyping.isVisible = true
        recycler.adapter = trackListAdapter
        binding.searchInputText.addTextChangedListener(searchTextWatcher)
        recycler.layoutManager = LinearLayoutManager(this)

        clearHistoryButton.isVisible = trackListAdapter.getTrackList().isEmpty() == false
        binding.textHintBeforeTyping.isVisible =
            sharedPreferences.getHistory().isEmpty() == false

        trackListAdapter.openPlayerActivity = object : TrackAdapter.OpenPlayerActivity {
            override fun openPlayerActivity(track: Track) {
                if (isClickAllowed) {
                    sharedPreferences.addTrackInHistory(track)
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
            binding.textHintBeforeTyping.isVisible = false
            clearHistoryButton.isVisible = false
            progressBar.isVisible = true

            searchTracksUseCase.doRequest(
                binding.searchInputText.text.toString(),
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
                                trackListAdapter.setData(findTracks)
                                mainThreadHandler?.post(successListUiElements())
                                Log.e("resultCode", "$response, $findTracks")
                            }
                        }
                    }
                })
        }
    }

    private fun successListUiElements() = Runnable {
        progressBar.isVisible = false
        nothingImage.isVisible = false
        connectionProblemError.isVisible = false
        binding.historyLayout.isVisible = true
    }

    private fun nothingFoundUiElements() = Runnable {
        progressBar.isVisible = false
        nothingImage.isVisible = true
        connectionProblemError.isVisible = false
        recycler.isVisible = false
    }

    private fun connectionErrorUiElements() = Runnable {
        progressBar.isVisible = false
        nothingImage.isVisible = false
        connectionProblemError.isVisible = true
        recycler.isVisible = false
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
