package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.IntentFilter
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.app.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.app.LostConnectionBroadcastReceiver
import com.example.playlistmaker.app.SEARCH_REFRESH_RATE
import com.example.playlistmaker.app.SEARCH_UI_STATE_FILLED
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOCONNECTION
import com.example.playlistmaker.app.SEARCH_UI_STATE_NOTHINGFOUND
import com.example.playlistmaker.app.SEARCH_UI_STATE_PROGRESS
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var nothingImage: LinearLayout
    private lateinit var connectionProblemError: LinearLayout
    private lateinit var progressBar: FrameLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var recycler: RecyclerView
    private lateinit var binding: FragmentSearchBinding
    private lateinit var clearButton: ImageView
    private lateinit var reloadButton: Button

    private val vm by viewModel<SearchViewModel>()
    private val br by lazy { LostConnectionBroadcastReceiver(requireView()) }

    private val adapter = TrackAdapter()
    private var isClickAllowed = true
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
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
        reloadButton.setOnClickListener { searchAction() }
        clearHistoryButton.setOnClickListener {
            vm.clearHistory()
            adapter.setData(emptyList())
        }
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
                    lifecycleScope.launch {
                        delay(CLICK_DEBOUNCE_DELAY)
                        isClickAllowed = true
                    }
                    val json = Gson().toJson(track)
                    findNavController()
                        .navigate(
                            R.id.action_fragmentSingleSearch_to_playerFragment,
                            PlayerFragment.createArgs(json)
                        )
                }
            }
        }
    }

    private fun searchAction() {
        val input = binding.searchInputText.text
        if (input.isNullOrEmpty()) return
        uiManagement(SEARCH_UI_STATE_PROGRESS)
        lifecycleScope.launch {
            adapter.setData(vm.searchAction(input.toString()))
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

    override fun onResume() {
        super.onResume()
        adapter.setData(vm.getHistory())
        searchAction()
        requireActivity().registerReceiver(br, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(br)
    }
}

