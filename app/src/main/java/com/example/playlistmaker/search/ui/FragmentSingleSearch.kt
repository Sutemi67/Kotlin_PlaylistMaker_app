package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.playlistmaker.databinding.FragmentSingleSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.TracksConsumer
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSingleSearch : Fragment() {

//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var nothingImage: LinearLayout
    private lateinit var connectionProblemError: LinearLayout
    private lateinit var progressBar: FrameLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentSingleSearchBinding
    private lateinit var clearButton: ImageView
    private lateinit var reloadButton: Button

    private val vm by viewModel<FragmentSingleSearchViewModel>()

    //    private val mainThreadHandler: Handler by inject()
    private val adapter = TrackAdapter()
    private var isClickAllowed = true
    private var isSearchAllowed = true
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearButton = binding.searchClearButton
        reloadButton = binding.reloadButton
        recycler = binding.searchList
        nothingImage = binding.nothingFound
        connectionProblemError = binding.connectionProblem
        progressBar = binding.progressBarLayout
        clearHistoryButton = binding.clearHistoryButton

        vm.isHistoryEmpty.observe(viewLifecycleOwner) { clearButtonManagement(it) }
        vm.uiState.observe(viewLifecycleOwner) { uiManagement(it) }

        clearButton.setOnClickListener {
            binding.searchInputText.text.clear()
            adapter.setData(vm.getHistory())
            uiManagement(SEARCH_UI_STATE_FILLED)
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                requireView().windowToken,
                0
            )
        }
//        reloadButton.setOnClickListener { mainThreadHandler.post(searchAction) }
        reloadButton.setOnClickListener { searchAction() }
        clearHistoryButton.setOnClickListener {
            vm.clearHistory()
            adapter.setData(emptyList())
        }
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                mainThreadHandler.post(searchAction)
                searchAction()
            }
            false
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    adapter.setData(vm.getHistory())
                    clearButton.isVisible = false
                    searchJob?.cancel()
                } else {
                    clearButton.isVisible = true
                    searchJob?.cancel()
                    if (searchJob == null || searchJob!!.isCancelled || searchJob!!.isCompleted) {
                        searchJob = lifecycleScope.launch {
                            delay(SEARCH_REFRESH_RATE)
                            searchAction()
                        }
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
        recycler.layoutManager = LinearLayoutManager(requireContext())

        adapter.openPlayerActivity = object : OpenPlayerActivity {
            override fun openPlayerActivity(track: Track) {
                if (isClickAllowed) {
                    vm.addTrackInHistory(track)
                    isClickAllowed = false
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(CLICK_DEBOUNCE_DELAY)
                        isClickAllowed = true
                    }
                    val intent = Intent(requireContext(), PlayerActivity::class.java)
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

//    private val allowingSearchAction: Runnable by lazy {
//        Runnable {
//            isSearchAllowed = true
//        }
//    }
//
//    private val startSearchAction: Runnable by lazy {
//        Runnable {
//            if (isSearchAllowed) {
//                isSearchAllowed = false
//                mainThreadHandler.post(searchAction)
//            }
//        }
//    }

    private fun searchAction() {
        if (binding.searchInputText.text.isNullOrEmpty()) return
        uiManagement(SEARCH_UI_STATE_PROGRESS)
        vm.searchAction(
            binding.searchInputText.text.toString(),
            object : TracksConsumer {
                override fun consume(findTracks: List<Track>, response: Int) {
//                    mainThreadHandler.post {
//                    searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    if (findTracks.isEmpty()) {
                        if (response == 400) {
                            vm.setUIState(SEARCH_UI_STATE_NOCONNECTION)
//                                searchJob?.cancel()
                        }
                        vm.setUIState(SEARCH_UI_STATE_NOTHINGFOUND)
                    } else {
                        adapter.setData(findTracks)
                        vm.setUIState(SEARCH_UI_STATE_FILLED)
//                        }
                    }
                }
            })
    }


//    private val searchAction: Runnable by lazy {
//        Runnable {
//            if (binding.searchInputText.text.isNullOrEmpty()) return@Runnable
//            uiManagement(SEARCH_UI_STATE_PROGRESS)
//            vm.searchAction(
//                binding.searchInputText.text.toString(),
//                object : TracksConsumer {
//                    override fun consume(findTracks: List<Track>, response: Int) {
//                        mainThreadHandler.post {
//                            if (findTracks.isEmpty()) {
//                                if (response == 400) {
//                                    vm.setUIState(SEARCH_UI_STATE_NOCONNECTION)
//                                    return@post
//                                }
//                                vm.setUIState(SEARCH_UI_STATE_NOTHINGFOUND)
//                            } else {
//                                adapter.setData(findTracks)
//                                vm.setUIState(SEARCH_UI_STATE_FILLED)
//                            }
//                        }
//                    }
//                })
//        }
//    }

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

    companion object {

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSingleSearch().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}