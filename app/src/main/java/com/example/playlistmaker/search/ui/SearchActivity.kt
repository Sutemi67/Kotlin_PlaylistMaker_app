package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
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
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ARTIST
import com.example.playlistmaker.app.ARTWORK_URL
import com.example.playlistmaker.app.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.app.COLLECTION_NAME
import com.example.playlistmaker.app.COUNTRY
import com.example.playlistmaker.app.GENRE
import com.example.playlistmaker.app.PREVIEW_URL
import com.example.playlistmaker.app.RELEASE_DATE
import com.example.playlistmaker.app.SEARCH_REFRESH_RATE
import com.example.playlistmaker.app.SEARCH_UI_STATE_FILLED
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOCONNECTION
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOTHINGFOUND
import com.example.playlistmaker.app.SEARCH_UI_STATE_PROGRESS
import com.example.playlistmaker.app.TRACK_NAME
import com.example.playlistmaker.app.TRACK_TIME_IN_MILLIS
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.TracksConsumer
import com.example.playlistmaker.search.domain.models.Track
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var nothingImage: LinearLayout
    private lateinit var connectionProblemError: LinearLayout
    private lateinit var progressBar: FrameLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var recycler: RecyclerView
    private lateinit var binding: ActivitySearchBinding
    private lateinit var clearButton: ImageView
    private lateinit var reloadButton: Button

    private val vm by viewModel<SearchViewModel>()
    private val mainThreadHandler: Handler by inject()
    private val adapter = TrackAdapter()
    private var isClickAllowed = true
    private var isSearchAllowed = true

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
        clearButton = binding.searchClearButton
        reloadButton = binding.reloadButton
        binding.backIconSearchScreen2.setNavigationOnClickListener { finish() }
        recycler = binding.searchList
        nothingImage = binding.nothingFound
        connectionProblemError = binding.connectionProblem
        progressBar = binding.progressBarLayout
        clearHistoryButton = binding.clearHistoryButton

        vm.isHistoryEmpty.observe(this) { isEmpty ->
            clearButtonManagement(isEmpty)
        }
        vm.uiState.observe(this) { state ->
            uiManagement(state)
        }

        clearButton.setOnClickListener {
            binding.searchInputText.text.clear()
            adapter.setData(vm.getHistory())
            uiManagement(SEARCH_UI_STATE_FILLED)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                findViewById<View>(android.R.id.content).windowToken,
                0
            )
        }
        reloadButton.setOnClickListener { mainThreadHandler.post(searchAction()) }
        clearHistoryButton.setOnClickListener {
            vm.clearHistory()
            adapter.setData(emptyList())
        }
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mainThreadHandler.post(searchAction())
            }
            false
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                } else {
                    clearButton.isVisible = true
                    if (isSearchAllowed && s.isNotEmpty()) {
                        isSearchAllowed = false
                        mainThreadHandler.postDelayed(
                            searchAction(),
                            SEARCH_REFRESH_RATE
                        )
                        mainThreadHandler.postDelayed(
                            { isSearchAllowed = true },
                            SEARCH_REFRESH_RATE
                        )
                    }
                }
                if (vm.getHistory().isNotEmpty()) {
                    binding.historyLayout.isVisible =
                        binding.searchInputText.hasFocus() && s?.isEmpty() == true
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        adapter.setData(vm.getHistory())
        recycler.adapter = adapter
        binding.searchInputText.addTextChangedListener(searchTextWatcher)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter.openPlayerActivity = object : TrackAdapter.OpenPlayerActivity {
            override fun openPlayerActivity(track: Track) {
                if (isClickAllowed) {
                    vm.addTrackInHistory(track)
                    isClickAllowed = false
                    mainThreadHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
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
            if (binding.searchInputText.text.isNullOrEmpty()) return@Runnable

            uiManagement(SEARCH_UI_STATE_PROGRESS)
            vm.searchAction(
                binding.searchInputText.text.toString(),
                object : TracksConsumer {
                    override fun consume(findTracks: List<Track>, response: Int) {
                        mainThreadHandler.post {
                            if (findTracks.isEmpty()) {
                                if (response == 400) {
                                    vm.setUIState(SEARCH_UI_STATE_NOCONNECTION)
                                    return@post
                                }
                                vm.setUIState(SEARCH_UI_STATE_NOTHINGFOUND)
                            } else {
                                adapter.setData(findTracks)
                                vm.setUIState(SEARCH_UI_STATE_FILLED)
                            }
                        }
                    }
                })
        }
    }

    private fun clearButtonManagement(isEmpty: Boolean) {
        clearHistoryButton.isVisible = !isEmpty
        binding.textHintBeforeTyping.isVisible = !isEmpty
    }

    private fun uiManagement(state: Int) {
        when (state) {
            SEARCH_UI_STATE_NOCONNECTION -> {
                progressBar.isVisible = false
                nothingImage.isVisible = false
                connectionProblemError.isVisible = true
                binding.historyLayout.isVisible = false
            }

            SEARCH_UI_STATE_NOTHINGFOUND -> {
                progressBar.isVisible = false
                nothingImage.isVisible = true
                connectionProblemError.isVisible = false
                binding.historyLayout.isVisible = false

            }

            SEARCH_UI_STATE_FILLED -> {
                progressBar.isVisible = false
                nothingImage.isVisible = false
                connectionProblemError.isVisible = false
                binding.historyLayout.isVisible = true
                binding.textHintBeforeTyping.isVisible = false
                binding.clearHistoryButton.isVisible = false

            }

            SEARCH_UI_STATE_PROGRESS -> {
                progressBar.isVisible = true
                nothingImage.isVisible = false
                connectionProblemError.isVisible = false
                binding.historyLayout.isVisible = false
            }
        }
    }
}
