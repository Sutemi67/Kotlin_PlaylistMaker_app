package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FragmentFavourites : Fragment() {
    private val playlistViewModel by viewModel<FavouritesViewModel> {
        parametersOf(requireArguments().getString(FAVOURITES_ARGS))
    }
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(FAVOURITES_ARGS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    companion object {
        private const val FAVOURITES_ARGS = "favourites_parameter"

        @JvmStatic
        fun newInstance() =
            FragmentFavourites().apply {
                arguments = Bundle().apply {
                    putString(FAVOURITES_ARGS, param1)
                }
            }
    }
}